package com.essam.simpleplacepicker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.essam.simpleplacepicker.utils.FetchAddressIntentService
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.skyfishjy.library.RippleBackground
import java.util.*

open class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    //location
    private var mMap: GoogleMap? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLAstKnownLocation: Location? = null
    private var locationCallback: LocationCallback? = null
    private val DEFAULT_ZOOM = 17f

    //views
    private var mapView: View? = null
    private var rippleBg: RippleBackground? = null
    //private var mProgressBar: ProgressBar? = null
    private var mSmallPinIv: ImageView? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        initViews()
        receiveIntent()
        initMapsAndPlaces()
    }

    private fun initViews() {
        val submitLocationButton = findViewById<Button>(R.id.submit_location_button)
        rippleBg = findViewById(R.id.ripple_bg)
        //mProgressBar = findViewById(R.id.progress_bar)
        mSmallPinIv = findViewById(R.id.small_pin)
        val icPin = findViewById<View>(R.id.ic_pin)
        Handler().postDelayed({ revealView(icPin) }, 1000)
        submitLocationButton.setOnClickListener { submitResultLocation() }
    }

    private fun receiveIntent() {
        val intent = intent
        if (intent.hasExtra(SimplePlacePicker.API_KEY)) {
            mApiKey = intent.getStringExtra(SimplePlacePicker.API_KEY)
        }
        if (intent.hasExtra(SimplePlacePicker.COUNTRY)) {
            mCountry = intent.getStringExtra(SimplePlacePicker.COUNTRY)
        }
        if (intent.hasExtra(SimplePlacePicker.LANGUAGE)) {
            mLanguage = intent.getStringExtra(SimplePlacePicker.LANGUAGE)
        }
        if (intent.hasExtra(SimplePlacePicker.SUPPORTED_AREAS)) {
            mSupportedArea = intent.getStringArrayExtra(SimplePlacePicker.SUPPORTED_AREAS)
        }
    }

    private fun initMapsAndPlaces() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(this, mApiKey!!)
        val mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.view
    }

    private fun submitResultLocation() {
        // if the process of getting address failed or this is not supported area , don't submit
        if (addressResultCode == SimplePlacePicker.FAILURE_RESULT || !isSupportedArea) {
            Toast.makeText(this@MapActivity, R.string.failed_select_location, Toast.LENGTH_SHORT).show()
        } else {
            val data = Intent()
            data.putExtra(SimplePlacePicker.SELECTED_ADDRESS, addressOutput)
            data.putExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, currentMarkerPosition.latitude)
            data.putExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, currentMarkerPosition.longitude)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    /*
      is triggered when the map is loaded and ready to display
      @param GoogleMap
     *
     * */   override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.isMyLocationEnabled = true
        //enable location button
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.uiSettings.isCompassEnabled = false

        //move location button to the required position and adjust params such margin
        if (mapView != null && mapView!!.findViewById<View?>("1".toInt()) != null) {
            val locationButton =
                (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 60, 500)
        }
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        //if task is successful means the gps is enabled so go and get device location amd move the camera to that location
        task.addOnSuccessListener { deviceLocation }

        //if task failed means gps is disabled so ask user to enable gps
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MapActivity, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
        mMap!!.setOnMyLocationButtonClickListener {
            false
        }
        mMap!!.setOnCameraIdleListener {
            mSmallPinIv!!.visibility = View.GONE
            //mProgressBar!!.visibility = View.VISIBLE
            Log.i(TAG, "changing address")
            //                ToDo : you can use retrofit for this network call instead of using services
            //hint: services is just for doing background tasks when the app is closed no need to use services to update ui
            //best way to do network calls and then update user ui is Retrofit .. consider it
            startIntentService()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
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
                            mMap!!.moveCamera(
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
                                    mMap!!.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                mLAstKnownLocation!!.latitude,
                                                mLAstKnownLocation!!.longitude
                                            ), DEFAULT_ZOOM
                                        )
                                    )
                                    //remove location updates in order not to continues check location unnecessarily
                                    if (locationCallback != null)
                                        mFusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
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
                        Toast.makeText(this@MapActivity, "Unable to get last location ", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    protected fun startIntentService() {
        currentMarkerPosition = mMap!!.cameraPosition.target
        val resultReceiver = AddressResultReceiver(Handler())
        val intent = Intent(this, FetchAddressIntentService::class.java)
        intent.putExtra(SimplePlacePicker.RECEIVER, resultReceiver)
        intent.putExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, currentMarkerPosition.latitude)
        intent.putExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, currentMarkerPosition.longitude)
        intent.putExtra(SimplePlacePicker.LANGUAGE, mLanguage)
        startService(intent)
    }

    private fun updateUi() {
        // todo display address
        //mDisplayAddressTextView!!.visibility = View.VISIBLE
        //mProgressBar!!.visibility = View.GONE
        mMap!!.clear()
        if (addressResultCode == SimplePlacePicker.SUCCESS_RESULT) {
            //check for supported area
            if (isSupportedArea(mSupportedArea)) {
                //supported
                addressOutput = addressOutput!!.replace("Unnamed Road,", "")
                addressOutput = addressOutput!!.replace("Unnamed Road،", "")
                addressOutput = addressOutput!!.replace("Unnamed Road New,", "")
                mSmallPinIv!!.visibility = View.VISIBLE
                isSupportedArea = true
                //mDisplayAddressTextView!!.text = addressOutput
            } else {
                //not supported
                mSmallPinIv!!.visibility = View.GONE
                isSupportedArea = false
                //mDisplayAddressTextView!!.text = getString(R.string.not_support_area)
            }
        } else if (addressResultCode == SimplePlacePicker.FAILURE_RESULT) {
            mSmallPinIv!!.visibility = View.GONE
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

    private fun revealView(view: View) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
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

    companion object {
        private val TAG = MapActivity::class.java.simpleName
    }
}