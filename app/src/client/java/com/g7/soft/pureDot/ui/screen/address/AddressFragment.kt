package com.g7.soft.pureDot.ui.screen.address

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.essam.simpleplacepicker.utils.FetchAddressIntentService
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentAddressBinding
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.ClientRepository
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.fragment_address.view.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.hypot


open class AddressFragment : Fragment(), OnMapReadyCallback {

    // picker variables
    //location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLAstKnownLocation: Location? = null
    private var locationCallback: LocationCallback? = null
    private val DEFAULT_ZOOM = 17f

    //views
    //private var mProgressBar: ProgressBar? = null

    //variables
    private var addressOutput: String? = null
    private var addressResultCode = 0
    private var isSupportedArea = false
    private lateinit var currentMarkerPosition: LatLng

    //receiving
    private var mApiKey: String? = ""
    private var mSupportedArea: Array<String>? = arrayOf()
    private var mCountry: String? = ""
    private var mLanguage: String? = "en"


    private lateinit var binding: FragmentAddressBinding
    internal lateinit var viewModel: AddressViewModel
    private var googleMap: GoogleMap? = null
    private lateinit var locationVM: LocationVM
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_address,
            container,
            false
        )


        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        locationVM = ViewModelProvider(this).get(LocationVM::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup click listener
        binding.navMenuIv.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

                viewModel.addAddress(
                    langTag = requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                ).observeApiResponse(this@AddressFragment, {
                    // todo send created address with the return id to the previous screen
                    findNavController().popBackStack()
                })
            }
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // setup map
        (childFragmentManager.findFragmentById(R.id.pickerMap) as SupportMapFragment?)
            ?.getMapAsync(this)

        // setup bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val currentHeight: Int = binding.root.height - bottomSheet.height
                val bottomSheetShiftDown = currentHeight - bottomSheet.top

                googleMap?.setPadding(
                    0,
                    24.dpToPx(),
                    0,
                    binding.bottomSheet.height + bottomSheetShiftDown
                )
                if (locationVM.location?.value != null) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(
                            LatLng(
                                locationVM.location!!.value!!.latitude,
                                locationVM.location!!.value!!.longitude
                            )
                        ) // Sets the center of the map to location user
                        .zoom(17f) // Sets the zoom
                        .bearing(90f) // Sets the orientation of the camera to east
                        .tilt(40f) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition
                        )
                    )
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })

        // setup picker
        Handler().postDelayed({ revealView(binding.icPin) }, 1000)
        receiveIntent()
        initMapsAndPlaces()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.isMyLocationEnabled = true
        //enable location button
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isCompassEnabled = false

        //googleMap.uiSettings.isScrollGesturesEnabled = false
        //googleMap.uiSettings.isMapToolbarEnabled = false

        //move location button to the required position and adjust params such margin
        /*if (binding.pickerMap.findViewById<View?>("1".toInt()) != null) {
            val locationButton =
                (binding.pickerMap.findViewById<View>("1".toInt()).parent as View).findViewById<View>(
                    "2".toInt()
                )
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 60, 500)
        }*/

        googleMap.setPadding(0, 24.dpToPx(), 0, 110.dpToPx())

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = settingsClient.checkLocationSettings(builder.build())

        //if task is successful means the gps is enabled so go and get device location amd move the camera to that location
        task.addOnSuccessListener { deviceLocation }

        //if task failed means gps is disabled so ask user to enable gps
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(requireActivity(), 51)
                } catch (e1: IntentSender.SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
        googleMap.setOnMyLocationButtonClickListener {
            false
        }
        googleMap.setOnCameraIdleListener {
            binding.icPin.visibility = View.GONE
            //mProgressBar!!.visibility = View.VISIBLE
            Timber.i("changing address")
            //                ToDo : you can use retrofit for this network call instead of using services
            //hint: services is just for doing background tasks when the app is closed no need to use services to update ui
            //best way to do network calls and then update user ui is Retrofit .. consider it
            startIntentService()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                deviceLocation
            }
        }
    }//remove location updates in order not to continues check location unnecessarily

    /**
     * is triggered whenever we want to fetch device location
     * in order to get device's location we use FusedLocationProviderClient object that gives us the last location
     * if the task of getting last location is successful and not equal to null ,
     * apply this location to mLastLocation instance and move the camera to this location
     * if the task is not successful create new LocationRequest and LocationCallback instances and update lastKnownLocation with location result
     */
    @get:SuppressLint("MissingPermission")
    private val deviceLocation: Unit
        get() {
            mFusedLocationProviderClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mLAstKnownLocation = task.result
                        if (mLAstKnownLocation != null) {
                            googleMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        mLAstKnownLocation!!.latitude,
                                        mLAstKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM
                                )
                            )
                        } else {
                            val locationRequest = LocationRequest.create()
                            locationRequest.interval = 1000
                            locationRequest.fastestInterval = 5000
                            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            locationCallback = object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    super.onLocationResult(locationResult)
                                    if (mLAstKnownLocation == null) return
                                    mLAstKnownLocation = locationResult.lastLocation
                                    googleMap!!.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                mLAstKnownLocation!!.latitude,
                                                mLAstKnownLocation!!.longitude
                                            ), DEFAULT_ZOOM
                                        )
                                    )
                                    //remove location updates in order not to continues check location unnecessarily
                                    if (locationCallback != null)
                                        mFusedLocationProviderClient!!.removeLocationUpdates(
                                            locationCallback!!
                                        )
                                }
                            }

                            val looper = Looper.myLooper()
                            if (locationCallback != null && looper != null)
                                mFusedLocationProviderClient!!.requestLocationUpdates(
                                    locationRequest,
                                    locationCallback!!,
                                    looper
                                )
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Unable to get last location ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    protected fun startIntentService() {
        currentMarkerPosition = googleMap!!.cameraPosition.target
        val resultReceiver = AddressResultReceiver(Handler())
        val intent = Intent(requireContext(), FetchAddressIntentService::class.java)
        intent.putExtra(SimplePlacePicker.RECEIVER, resultReceiver)
        intent.putExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, currentMarkerPosition.latitude)
        intent.putExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, currentMarkerPosition.longitude)
        intent.putExtra(SimplePlacePicker.LANGUAGE, mLanguage)
        requireActivity().startService(intent)
    }

    private fun updateUi() {
        // todo display address
        //mDisplayAddressTextView!!.visibility = View.VISIBLE
        //mProgressBar!!.visibility = View.GONE
        googleMap!!.clear()
        if (addressResultCode == SimplePlacePicker.SUCCESS_RESULT) {
            //check for supported area
            if (isSupportedArea(mSupportedArea)) {
                //supported
                addressOutput = addressOutput!!.replace("Unnamed Road,", "")
                addressOutput = addressOutput!!.replace("Unnamed Road،", "")
                addressOutput = addressOutput!!.replace("Unnamed Road New,", "")
                binding.icPin.visibility = View.VISIBLE
                isSupportedArea = true
                //mDisplayAddressTextView!!.text = addressOutput
            } else {
                //not supported
                binding.icPin.visibility = View.GONE
                isSupportedArea = false
                //mDisplayAddressTextView!!.text = getString(R.string.not_support_area)
            }
        } else if (addressResultCode == SimplePlacePicker.FAILURE_RESULT) {
            binding.icPin.visibility = View.GONE
            //mDisplayAddressTextView!!.text = addressOutput
        }
    }

    private fun isSupportedArea(supportedAreas: Array<String>?): Boolean {
        if (supportedAreas!!.isEmpty()) return true

        var isSupported = false
        for (area in supportedAreas) {
            if (addressOutput!!.contains(area)) {
                isSupported = true
                break
            }
        }
        return isSupported
    }

    private fun initViews() {

    }

    private fun receiveIntent() {
        mApiKey = getString(R.string.google_api_key)
        mLanguage = requireActivity().currentLocale.toLanguageTag()
        // todo add country and supportedAreas if needed variables: mCountry, mSupportedArea
    }

    private fun initMapsAndPlaces() {
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //Places.initialize(requireContext(), mApiKey!!)
        (childFragmentManager.findFragmentById(R.id.pickerMap) as SupportMapFragment?)
            ?.getMapAsync(this)
        //binding.pickerMap = (childFragmentManager.findFragmentById(R.id.pickerMap) as SupportMapFragment?)?.view todo test
    }

    private fun revealView(view: View) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        view.visibility = View.VISIBLE
        anim.start()
    }


    internal inner class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            addressResultCode = resultCode

            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(SimplePlacePicker.RESULT_DATA_KEY)
            if (addressOutput == null) {
                addressOutput = ""
            }
            updateUi()
        }
    }
}