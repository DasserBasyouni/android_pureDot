package com.g7.soft.pureDot.ui.screen.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.g7.soft.pureDot.ext.bitmapDescriptorFromVector
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.PermissionsHelper
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.driver.fragment_home.view.*


open class HomeFragment : Fragment(), OnMapReadyCallback {

    // location
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val DEFAULT_ZOOM = 17f

    private lateinit var binding: FragmentHomeBinding
    internal lateinit var viewModel: HomeViewModel
    private var googleMap: GoogleMap? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup sideNavMenu items
        /*val sideNavMenuAdapter = SideNavMenuAdapter(this) todo uncomment this
        binding.sideNavMenuLayout.navMenuRv.adapter = sideNavMenuAdapter*/

        // add decoration divider
        binding.sideNavMenuLayout.navMenuRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // fetch data
        val tokenId = "" // todo
        viewModel.fetchData(
            requireActivity().currentLocale.toLanguageTag(),
            tokenId = tokenId
        )

        // setup observables
        viewModel.location.observe(viewLifecycleOwner, {
            Log.e("Z_", "location: $it")
            if (it != null) {
                val tokenId = "" // todo

                Log.e(
                    "Z_",
                    "lo1: ${viewModel.ordersResponse.value == null && viewModel.isAvailable.value == true}"
                )
                if (viewModel.ordersResponse.value == null && viewModel.isAvailable.value == true)
                    viewModel.getPendingOrders(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        lat = it.latitude,
                        lng = it.longitude
                    )

                // setup location icon
                googleMap?.clear()

                val locationMarkerOptions = MarkerOptions()
                locationMarkerOptions.icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_driver_location_pin))
                locationMarkerOptions.position(LatLng(it.latitude, it.longitude))

                googleMap?.addMarker(locationMarkerOptions)

                // animate camera to location
                googleMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        DEFAULT_ZOOM
                    ), 4000, null
                )

            }
        })

        viewModel.availabilityResponse.observe(viewLifecycleOwner, {
            viewModel.availabilityLcee.value!!.response.value = it
            binding.isAvailableS.isChecked = it.data?.isAvailable ?: false
        })

        /*val ordersAdapter = PendingOrdersAdapter(this) todo uncomment this
        binding.ordersRv.adapter = ordersAdapter
        viewModel.ordersResponse.observe(viewLifecycleOwner, {
            //Log.e("Z_", "it: ${it.status} - ${it.data?.size}")
            viewModel.ordersLcee.value!!.response.value = it
            ordersAdapter.submitList(it?.data)
        })*/

        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it
            //sideNavMenuAdapter.updateWallet(it.data?.balance, it.data?.currency)
        })

        viewModel.selectedOrder.observe(viewLifecycleOwner, {
            binding.productsRv.adapter = ProductsAdapter().apply { this.submitList(it?.products) }
        })

        // setup map
        (childFragmentManager.findFragmentById(R.id.pickerMap) as SupportMapFragment?)
            ?.getMapAsync(this)

        // setup click listener
        binding.screenClickableIv.setOnClickListener {
            closeSideNavigationMenu()
        }

        binding.upButtonIv.setOnClickListener {
            viewModel.selectedOrder.value = null
        }

        viewModel.isAvailable.observe(viewLifecycleOwner, {
            val tokenId = "" // todo
            Log.e(
                "Z_",
                "isAvailable.observe: ${!viewModel.isFirstFetchData} - ${viewModel.isFirstFetchData && viewModel.availabilityResponse.value?.status != ProjectConstant.Companion.Status.LOADING}"
            )

            if (!viewModel.isFirstFetchData) {
                // fetch pending orders for the first time
                getOrRemovePendingOrders(tokenId)

            } else if (viewModel.isFirstFetchData && viewModel.availabilityResponse.value?.status != ProjectConstant.Companion.Status.LOADING) {

                viewModel.changeAvailability(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                ).observe(viewLifecycleOwner, {

                    // show loading
                    if (it.status == ProjectConstant.Companion.Status.LOADING)
                        ProjectDialogUtils.showLoading(requireContext())
                    else
                        ProjectDialogUtils.hideLoading()

                    // reset the switch if have error
                    if (it.status == ProjectConstant.Companion.Status.API_ERROR
                        || it.status == ProjectConstant.Companion.Status.NETWORK_ERROR
                    ) {
                        // apply availability to switch
                        binding.isAvailableS.isChecked = !binding.isAvailableS.isChecked

                        getOrRemovePendingOrders(tokenId)
                    }
                })
            } else if (!viewModel.isFirstFetchData)
                viewModel.isFirstFetchData = true
        })

        binding.navMenuIv.setOnClickListener {
            if (viewModel.isSideNavMenuOpened.value == true)
                closeSideNavigationMenu()
            else
                openSideNavigationMenu()
        }

        binding.positiveBtn.setOnClickListener {
            val nextStatus =
                when (ApiConstant.OrderDeliveryStatus.fromInt(viewModel.selectedOrder.value?.status)) {
                    ApiConstant.OrderDeliveryStatus.NEW -> ApiConstant.OrderDeliveryStatus.ACCEPTED.value
                    else -> null
                }

            val tokenId = "" // todo
            viewModel.changeOrderStatus(
                langTag = requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                orderNumber = viewModel.selectedOrder.value?.number,
                status = nextStatus
            ).observeApiResponse(this, {
                viewModel.selectedOrder.value?.status = ApiConstant.OrderDeliveryStatus.ACCEPTED.value
            })
        }

        binding.rejectBtn.setOnClickListener {
            val tokenId = "" // todo
            viewModel.changeOrderStatus(
                langTag = requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                orderNumber = viewModel.selectedOrder.value?.number,
                status = ApiConstant.OrderDeliveryStatus.REJECTED.value
            ).observeApiResponse(this, {
                viewModel.ordersResponse.value =
                    viewModel.ordersResponse.value.also {
                        it?.data?.removeAt(it.data.indexOfFirst { order ->
                            order.number == viewModel.selectedOrder.value?.number
                        })
                    }
                viewModel.selectedOrder.value = null
            })
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.bottomSheet)
        /*binding.bottomSheet.setOnClickListener {
            bottomSheetBehavior.state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
        }*/


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
                    binding.bottomSheet.height + bottomSheetShiftDown - 42.dpToPx()
                )
                if (viewModel.location.value != null) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(
                            LatLng(
                                viewModel.location.value!!.latitude,
                                viewModel.location.value!!.longitude
                            )
                        ) // Sets the center of the map to location user
                        .zoom(DEFAULT_ZOOM) // Sets the zoom
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
    }

    private fun getOrRemovePendingOrders(tokenId: String) {
        if (viewModel.location.value != null) {
            Log.e("Z_", "lo2: ${viewModel.isAvailable.value == true}")
            if (viewModel.isAvailable.value == true) {
                viewModel.getPendingOrders(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    lat = viewModel.location.value?.latitude,
                    lng = viewModel.location.value?.longitude
                )
            } else
                viewModel.ordersResponse.value = null
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        PermissionsHelper.requestLocationPermission(requireContext(), grantedRunnable = {
            //googleMap.isMyLocationEnabled = true
            //enable location button
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isCompassEnabled = false

            googleMap.setPadding(0, 24.dpToPx(), 0, 312.dpToPx() - 42.dpToPx())

            Log.e("Z_", "fusedLocationClient: ${fusedLocationClient != null}")
            fusedLocationClient?.lastLocation
                ?.addOnSuccessListener(
                    requireActivity()
                ) { location ->
                    Log.e("Z_", "looooooo: ${location != null}")
                    // Got last known location. In some rare situations this can be null.
                    if (location != null)
                        viewModel.location.value = location
                }
        })

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
    }


    private fun openSideNavigationMenu() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root as ConstraintLayout)

        constraintSet.constrainWidth(binding.coordinatorLayout.id, (screenWidth * .60).toInt())

        constraintSet.connect(
            binding.coordinatorLayout.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            0
        )
        constraintSet.setMargin(
            binding.coordinatorLayout.id, ConstraintSet.TOP,
            (screenHeight * .20).toInt()
        )
        constraintSet.setMargin(
            binding.coordinatorLayout.id, ConstraintSet.BOTTOM,
            (screenHeight * .20).toInt()
        )

        constraintSet.applyTo(binding.root as ConstraintLayout)

        viewModel.isSideNavMenuOpened.value = true

        binding.screenClickableIv.visibility = View.VISIBLE
    }

    fun closeSideNavigationMenu(onFinishRunnable: Runnable? = null) {
        binding.screenClickableIv.visibility = View.GONE

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root as ConstraintLayout)

        constraintSet.constrainWidth(binding.coordinatorLayout.id, 0)

        constraintSet.connect(
            binding.coordinatorLayout.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0
        )
        constraintSet.setMargin(
            binding.coordinatorLayout.id, ConstraintSet.TOP, 0
        )
        constraintSet.setMargin(binding.coordinatorLayout.id, ConstraintSet.BOTTOM, 0)

        val transition = ChangeBounds()
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                onFinishRunnable?.run()
            }

            override fun onTransitionResume(transition: Transition) = Unit

            override fun onTransitionPause(transition: Transition) = Unit

            override fun onTransitionCancel(transition: Transition) = Unit

            override fun onTransitionStart(transition: Transition) = Unit
        })

        TransitionManager.beginDelayedTransition(binding.constraintLayout, transition)

        constraintSet.applyTo(binding.root as ConstraintLayout)

        viewModel.isSideNavMenuOpened.value = false
    }

    fun doBackPressed() {
        if (viewModel.selectedOrder.value != null)
            viewModel.selectedOrder.value = null
        else
            (requireActivity() as MainActivity).superOnBackPressed()
    }

}