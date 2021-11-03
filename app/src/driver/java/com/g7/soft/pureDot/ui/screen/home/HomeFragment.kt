package com.g7.soft.pureDot.ui.screen.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.work.*
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PendingOrdersAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.SideNavMenuAdapter
import com.g7.soft.pureDot.constant.ApiConstant.OrderDeliveryStatus
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.REQUEST_ENABLE_LOCATION_SETTINGS
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.utils.MapBoxUtils
import com.g7.soft.pureDot.utils.PermissionsHelper
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.g7.soft.pureDot.worker.LocationUploadWorker
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria.GEOMETRY_POLYLINE6
import com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.driver.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


//import com.mapbox.navigation.core.trip.service.MapboxTripService

open class HomeFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    private var clientSymbol: Symbol? = null
    private var branchSymbol: Symbol? = null
    private lateinit var permissionsManager: PermissionsManager
    //private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        var refreshData: ((String?) -> Unit)? = null
        var isRunning = false
    }

    val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK)
            setupTheScreen()
        else
            requireActivity().finish()
    }

    private val CLIENT_ICON_ID = "CLIENT_ICON_ID"
    private val BRANCH_ICON_ID = "BRANCH_ICON_ID"

    internal lateinit var binding: FragmentHomeBinding
    internal lateinit var viewModelFactory: HomeViewModelFactory
    internal lateinit var viewModel: HomeViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    /* ----- Mapbox Maps components ----- */
    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapboxSdkMapView: com.mapbox.mapboxsdk.maps.MapboxMap

    /* ----- Mapbox Navigation components ----- */
    private lateinit var mapboxNavigation: MapboxNavigation

    // location puck integration
    private val navigationLocationProvider = NavigationLocationProvider()

    // camera
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            20.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }

    // trip progress bottom view
    private lateinit var tripProgressApi: MapboxTripProgressApi

    // voice instructions
    private var isVoiceInstructionsMuted = false
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var speechAPI: MapboxSpeechApi
    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer

    // route line
    private lateinit var routeLineAPI: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var routeArrowView: MapboxRouteArrowView
    private val routeArrowAPI: MapboxRouteArrowApi = MapboxRouteArrowApi()

    /* ----- Voice instruction callbacks ----- */
    private val voiceInstructionsObserver =
        VoiceInstructionsObserver { voiceInstructions ->
            speechAPI.generate(
                voiceInstructions,
                speechCallback
            )
        }

    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
            // remove already consumed file to free-up space
            speechAPI.clean(value)
        }

    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
                    // play the instruction via fallback text-to-speech engine
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
                    // play the sound file from the external generator
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }

    /* ----- Location and route progress callbacks ----- */
    private val locationObserver = object : LocationObserver {
        override fun onRawLocationChanged(rawLocation: Location) {
            // not handled
        }

        override fun onEnhancedLocationChanged(
            enhancedLocation: Location,
            keyPoints: List<Location>
        ) {
            // update location puck's position on the map
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = keyPoints
            )

            // update camera position to account for new location
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()
        }
    }

    private val routeProgressObserver =
        RouteProgressObserver { routeProgress -> // update the camera position to account for the progressed fragment of the route
            viewportDataSource.onRouteProgressChanged(routeProgress)
            viewportDataSource.evaluate()

            // show arrow on the route line with the next maneuver
            val maneuverArrowResult = routeArrowAPI.addUpcomingManeuverArrow(routeProgress)
            val style = mapboxMap.getStyle()
            if (style != null) {
                routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
            }

            // update top maneuver instructions
            val maneuvers = maneuverApi.getManeuvers(routeProgress)
            maneuvers.fold(
                { error ->
                    Toast.makeText(requireActivity(), error.errorMessage, Toast.LENGTH_SHORT).show()
                }, {
                    //if (viewModel.isNavigationView.value == true) {
                    //binding.maneuverView.visibility = View.VISIBLE
                    binding.maneuverView.renderManeuvers(maneuvers)
                    //}
                }
            )

            // update bottom trip progress summary
            binding.tripProgressView.render(tripProgressApi.getTripProgress(routeProgress))
        }

    private val routesObserver = RoutesObserver { routes ->
        if (routes.isNotEmpty()) {
            // generate route geometries asynchronously and render them
            CoroutineScope(Dispatchers.Main).launch {
                val result = routeLineAPI.setRoutes(
                    listOf(RouteLine(routes.first(), null))
                )
                // source of code: https://github.com/mapbox/mapbox-navigation-android/issues/4400
                { value ->
                    mapboxMap.getStyle()?.apply {
                        routeLineView.renderRouteDrawData(this, value)
                    }
                }
                /*val style = mapboxMap.getStyle()
                if (style != null) {
                    routeLineView.renderRouteDrawData(style, result)
                }*/
            }

            // update the camera position to account for the new route
            viewportDataSource.onRouteChanged(routes.first())
            viewportDataSource.evaluate()
        } else {
            // remove the route line and route arrow from the map
            val style = mapboxMap.getStyle()
            if (style != null) {
                routeLineAPI.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowAPI.clearArrows())
            }

            // remove the route reference to change camera position
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapboxSdkMapView.onResume()
        //if (::fusedLocationClient.isInitialized) startLocationUpdates()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        binding.mapboxSdkMapView.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
        OrderFragment.isRunning = true
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        binding.mapboxSdkMapView.onStop()
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
        OrderFragment.isRunning = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapboxSdkMapView.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // setup map instance (non-navigation one)
        Mapbox.getInstance(requireContext(), MapBoxUtils.getMapboxAccessToken(requireContext()))

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        mapboxMap = binding.mapView.getMapboxMap()

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())
            val tokenId = UserRepository("").getTokenId(requireContext())

            viewModelFactory = HomeViewModelFactory(
                tokenId = tokenId
            )
            viewModel = ViewModelProvider(
                this@HomeFragment,
                viewModelFactory
            ).get(HomeViewModel::class.java)

            binding.currency = currencySymbol
            binding.lifecycleOwner = this@HomeFragment
            binding.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refreshData = { _ -> // orderId
            viewModel.selectedOrder.value = null
            viewModel.ordersPagedList?.value?.dataSource?.invalidate()
        }

        /*val mapboxTripNotification = MapboxTripNotification(
            context, NavigationOptions.Builder()
                .distanceFormatter(mapboxDistanceFormatterObject)
                .timeFormatType(TimeFormatType.TWELVE_HOURS)
                .build()
        )
        val mapboxTripService = MapboxTripService(requireContext(), mapboxTripNotification, null)*/


        // initialize Mapbox Navigation
        /*val options1: MapboxNavigationOptions =
            MapboxNavigationOptions.builder().navigationNotification(mCustomNavigationNotification)
                .maneuverZoneRadius(70)
                .build()
        val options: NavigationViewOptions = NavigationViewOptions.builder()
            .directionsRoute(currentRoute)
            .directionsLanguage(Locale.getDefault())
            .navigationOptions(options1)
            .awsPoolId(null).shouldSimulateRoute(simulateRoute).build()*/

        mapboxNavigation = MapboxNavigation(
            NavigationOptions.Builder(requireContext())
                .accessToken(getMapboxAccessTokenFromResources())
                .build()
        )
        viewportDataSource = MapboxNavigationViewportDataSource(
            binding.mapView.getMapboxMap()
        )
        navigationCamera = NavigationCamera(
            binding.mapView.getMapboxMap(),
            binding.mapView.camera,
            viewportDataSource
        )

        setupTheScreen()

        // setup
        binding.mapboxSdkMapView.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        binding.mapboxSdkMapView.onDestroy()
        if (::mapboxNavigation.isInitialized)
            mapboxNavigation.onDestroy()
        if (::speechAPI.isInitialized)
            speechAPI.cancel()
        if (::voiceInstructionsPlayer.isInitialized)
            voiceInstructionsPlayer.shutdown()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
        binding.mapboxSdkMapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("Z_", "onActivityResult")
        Log.e("Z_", "$requestCode, $resultCode")

        if (requestCode == REQUEST_ENABLE_LOCATION_SETTINGS)
            setupTheScreen()
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: com.mapbox.mapboxsdk.maps.MapboxMap) {
        Log.e("ZZ_", "onMapReady")
        this@HomeFragment.mapboxSdkMapView = mapboxMap

        mapboxMap.uiSettings.setCompassMargins(8.dpToPx(), 32.dpToPx(), 8.dpToPx(), 0)

        // update current location
        val DEFAULT_INTERVAL_IN_MILLISECONDS = 15.toLong()
        val DEFAULT_MAX_WAIT_TIME = 15.toLong()
        val callback = object : LocationEngineCallback<LocationEngineResult> {
            override fun onSuccess(result: LocationEngineResult?) {
                Log.e("ZZ_", "onSuccess")
                // first time load
                if (result?.lastLocation != null && viewModel.isFirstFetchData) {
                    val position: CameraPosition = CameraPosition.Builder()
                        .target(
                            LatLng(
                                result.lastLocation!!.latitude,
                                result.lastLocation!!.longitude
                            )
                        ) // Sets the new camera position
                        .zoom(17.0) // Sets the zoom
                        .bearing(180.0) // Rotate the camera
                        .tilt(30.0) // Set the camera tilt
                        .build() // Creates a CameraPosition from the builder

                    mapboxMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(position), 5000
                    )
                }

                viewModel.location.value = result?.lastLocation
            }

            override fun onFailure(e: java.lang.Exception) {
                Log.e("ZZ_", "fail: $e")
                Timber.e(e)
            }
        }
        val locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()
        locationEngine.requestLocationUpdates(request, callback, getMainLooper())
        locationEngine.getLastLocation(callback)


        mapboxMap.uiSettings.setLogoMargins(
            8.dpToPx(),
            0,
            8.dpToPx(),
            110.dpToPx() + 8.dpToPx()
        )

        mapboxMap.setStyle(
            com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
        ) { style ->
            enableLocationPlugin(style)

            // add assets to map style
            val clientPinBitmap =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin)?.toBitmap()
                    ?: return@setStyle
            val branchPinBitmap =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_pin_branch)
                    ?.toBitmap()
                    ?: return@setStyle

            style.addImage(CLIENT_ICON_ID, clientPinBitmap)
            style.addImage(BRANCH_ICON_ID, branchPinBitmap)

            addMarkersToMap()
        }
    }

    private fun addMarkersToMap() {
        if (clientSymbol != null || branchSymbol != null) return

        val selectedOrder = viewModel.selectedOrder.value ?: return
        val clientLat = selectedOrder.clientAddress?.latitude?.toDoubleOrNull()
        val clientLng = selectedOrder.clientAddress?.longitude?.toDoubleOrNull()
        val branchLat = selectedOrder.firstOrder?.branch?.latitude?.toDoubleOrNull()
        val branchLng = selectedOrder.firstOrder?.branch?.longitude?.toDoubleOrNull()

        if (branchLat != null && branchLng != null && clientLat != null && clientLng != null) {
            val clientLatLng = LatLng(clientLat, clientLng)
            val branchLatLng = LatLng(branchLat, branchLng)

            clientSymbol = addMarker(LatLng(clientLat, clientLng), CLIENT_ICON_ID)
            branchSymbol = addMarker(LatLng(branchLat, branchLng), BRANCH_ICON_ID)

            if (clientSymbol != null && branchSymbol != null) {
                val lastKnownLocation =
                    mapboxSdkMapView.locationComponent.lastKnownLocation ?: return
                val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                    .include(
                        LatLng(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude
                        )
                    ) // Northeast
                    .include(branchLatLng) // Southwest
                    .include(clientLatLng)
                    .build()

                mapboxSdkMapView.easeCamera(
                    CameraUpdateFactory.newLatLngBounds(latLngBounds, 56.dpToPx()),
                    5000
                )
            }
        }
    }

    private fun addMarker(latLng: LatLng, iconId: String): Symbol? {
        // Create a SymbolManager.
        val symbolManager = SymbolManager(
            binding.mapboxSdkMapView, mapboxSdkMapView,
            mapboxSdkMapView.style ?: return null
        )

        // Set non-data-driven properties.
        symbolManager.iconAllowOverlap = true
        symbolManager.iconIgnorePlacement = true

        val symbolOptions = SymbolOptions()
            .withLatLng(latLng)
            .withIconImage(iconId)
            .withIconSize(1.3f)

        // Create a symbol at the specified location.
        return symbolManager.create(symbolOptions)
    }


    @SuppressLint("MissingPermission")
    private fun enableLocationPlugin(loadedMapStyle: com.mapbox.mapboxsdk.maps.Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            val locationComponent: LocationComponent = mapboxSdkMapView.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle).build()
            )
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(requireActivity())
        }
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

    /*@SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        var areaName: String? = null
                        try {
                            val listAddresses: List<Address>? =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (null != listAddresses && listAddresses.isNotEmpty()) {
                                areaName = listAddresses[0].getAddressLine(0)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        lifecycleScope.launch {
                            val tokenId = UserRepository("").getTokenId(requireContext())

                            viewModel.setCurrentLocation(
                                requireActivity().currentLocale.toLanguageTag(),
                                tokenId,
                                location.latitude,
                                location.longitude,
                                areaName
                            )
                        }
                    }
                }
            },
            Looper.getMainLooper()
        )
    }*/

    @SuppressLint("MissingPermission")
    private fun setupTheScreen() {
        Log.e("Z_", "it: setupTheScreen")
        PermissionsHelper.requestLocationPermission(requireContext(), resolutionForResult, {
            Log.e("Z_", "it: requestLocationPermission")
            binding.mapboxSdkMapView.getMapAsync(this)
            requireContext().startUploadingLocation()
            //fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            setupBottomSheet()
            setupSideNavigationMenu()
            setupObservables()
            fetchData()
            setupOnClick()
        }, deniedRunnable = { requireActivity().finish() })
    }

    @SuppressLint("MissingPermission")
    private fun setupNavigationMap() {
        // initialize the location puck
        binding.mapView.location.apply {
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.navigation_puck_icon
                )
            )
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }

        // setup map ui
        //binding.mapView.get.
        binding.mapView.logo.marginBottom =
            (384.dpToPx() - 110.dpToPx() - 204.dpToPx() - 26.dpToPx()).toFloat()

        // setup map bottom padding
        binding.mapView.setPadding(
            0,
            0,
            0,
            384.dpToPx() - 110.dpToPx()
        )

        // move the camera to current location on the first update
        mapboxNavigation.registerLocationObserver(object : LocationObserver {
            override fun onRawLocationChanged(rawLocation: Location) {
                val point = Point.fromLngLat(rawLocation.longitude, rawLocation.latitude)
                val cameraOptions = CameraOptions.Builder()
                    .center(point)
                    .zoom(13.0)
                    .build()
                mapboxMap.setCamera(cameraOptions)
                mapboxNavigation.unregisterLocationObserver(this)
            }

            override fun onEnhancedLocationChanged(
                enhancedLocation: Location,
                keyPoints: List<Location>
            ) {
                // not handled
            }
        })

        // initialize Navigation Camera
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState -> // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                    //NavigationCameraState.FOLLOWING -> binding.recenter.visibility = View.INVISIBLE

                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                    //NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
                NavigationCameraState.IDLE -> {
                }
                NavigationCameraState.FOLLOWING -> {
                }
            }
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }

        // initialize top maneuver view
        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(DistanceFormatterOptions.Builder(requireContext()).build())
        )

        // initialize bottom progress view
        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(requireContext())
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(
                        mapboxNavigation.navigationOptions.distanceFormatterOptions
                    )
                )
                .timeRemainingFormatter(TimeRemainingFormatter(requireContext()))
                .percentRouteTraveledFormatter(PercentDistanceTraveledFormatter())
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(requireContext(), TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )

        // initialize voice instructions
        speechAPI = MapboxSpeechApi(
            requireContext(),
            getMapboxAccessTokenFromResources(),
            Locale.US.language
        )
        voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
            requireContext(),
            getMapboxAccessTokenFromResources(),
            Locale.US.language
        )

        // initialize route line
        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(requireContext())
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineAPI = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)
        val routeArrowOptions = RouteArrowOptions.Builder(requireContext()).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        // load map style
        mapboxMap.loadStyleUri(
            Style.MAPBOX_STREETS
        ) { // add long click listener that search for a route to the clicked destination
            /*binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(point)
                true
            }*/
        }

        // initialize view interactions
        binding.stop.setOnClickListener {
            clearRouteAndStopNavigation()
            viewModel.isNavigationView.value = false
        }
        /*binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
        }*/
        /*binding.routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            //binding.recenter.showTextAndExtend(2000L)
        }*/
        binding.soundButton.setOnClickListener {
            // mute/unmute voice instructions
            isVoiceInstructionsMuted = !isVoiceInstructionsMuted
            if (isVoiceInstructionsMuted) {
                binding.soundButton.muteAndExtend(2000L)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                binding.soundButton.unmuteAndExtend(2000L)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }

        // start the trip session to being receiving location updates in free drive
        // and later when a route is set, also receiving route progress updates
        mapboxNavigation.startTripSession()
    }

    private fun findRoute(destination1: Point, destination2: Point) {
        /*val origin = navigationLocationProvider.lastLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return*/
        val origin = viewModel.location.value?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        Log.e(
            "Z_",
            "findRoute: $origin $destination1 == [${origin.latitude()}, ${origin.latitude()}] $destination2"
        )

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(requireContext())
                //.accessToken(getMapboxAccessTokenFromResources())
                .coordinatesList(listOf(origin, destination1, destination2)) // TODO Eng. Omnia
                .geometries(GEOMETRY_POLYLINE6)
                .profile(PROFILE_DRIVING)
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) = setRouteAndStartNavigation(routes.first(), routerOrigin)

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                    Timber.e("requestRoutes.onFailure")
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    Timber.e("requestRoutes.onCanceled")
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(route: DirectionsRoute, routerOrigin: RouterOrigin) {
        Log.e("Z_", "setRouteAndStartNavigation")
        // set route
        mapboxNavigation.setRoutes(listOf(route)) // TODO Eng. Omnia

        // show UI elements
        binding.soundButton.visibility = View.VISIBLE
        //binding.routeOverview.visibility = View.VISIBLE
        //binding.tripProgressCard.visibility = View.VISIBLE
        //binding.routeOverview.showTextAndExtend(2000L)
        binding.soundButton.unmuteAndExtend(2000L)

        // move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview() // TODO Eng. Omnia
    }

    private fun clearRouteAndStopNavigation() {
        // clear
        mapboxNavigation.setRoutes(listOf())

        // hide UI elements
        binding.soundButton.visibility = View.INVISIBLE
        //binding.maneuverView.visibility = View.INVISIBLE
        //binding.routeOverview.visibility = View.INVISIBLE
        binding.tripProgressCard.visibility = View.INVISIBLE
    }

    private fun getMapboxAccessTokenFromResources(): String =
        MapBoxUtils.getMapboxAccessToken(requireContext())

    private fun setupSideNavigationMenu() {
        val sideNavMenuAdapter = SideNavMenuAdapter(this)
        binding.sideNavMenuLayout.navMenuRv.adapter = sideNavMenuAdapter

        // add decoration divider
        binding.sideNavMenuLayout.navMenuRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup observable
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

                viewModel.userDataLcee.value!!.response.value = it
                sideNavMenuAdapter.updateWallet(it.data?.balance, currencySymbol)
            }
        })
    }

    private fun setupObservables() {
        viewModel.isNavigationView.observe(viewLifecycleOwner, {
            val logoBottomMargin: Float
            val mapBottomMargin: Int

            if (it == true) {
                navigationCamera.requestNavigationCameraToFollowing() // TODO Eng. Omnia
                logoBottomMargin = 146.dpToPx().toFloat()
                mapBottomMargin = 0.dpToPx()
                setupNavigationMap()
                findNavigationViewRoute()
            } else {
                navigationCamera.requestNavigationCameraToOverview()
                logoBottomMargin = 24.dpToPx().toFloat()
                mapBottomMargin = 164.dpToPx()
            }

            // setup mapbox logo padding
            binding.mapView.logo.marginBottom = logoBottomMargin

            // setup map bottom padding
            binding.mapView.setPadding(
                0,
                0,
                0,
                mapBottomMargin
            )
        })

        viewModel.availabilityResponse.observe(viewLifecycleOwner, {
            viewModel.availabilityLcee.value!!.response.value = it
            if (it?.data?.isAvailable != null && viewModel.isAvailable.value != it.data.isAvailable)
                viewModel.isAvailable.value = it.data.isAvailable
            //viewModel.isFirstFetchData = true
        })

        // setup pagination
        viewModel.ordersPagedList =
            PaginationDataSource.initializedPagedListBuilder<MasterOrderModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@HomeFragment,
            ).build()

        // setup observers
        val ordersAdapter = PendingOrdersAdapter(this)
        binding.ordersRv.adapter = ordersAdapter
        viewModel.ordersPagedList?.observe(viewLifecycleOwner, {
            ordersAdapter.submitList(it)
        })

        viewModel.selectedOrder.observe(viewLifecycleOwner, {
            binding.productsRv.adapter =
                ProductsAdapter().apply { this.submitList(it?.firstOrder?.products) }

            val isCollapsedSheet = true
            /*val isCollapsedSheet =
                !(it != null && it.firstOrder?.deliveryStatus != OrderDeliveryStatus.ACCEPTED.value
                        && it.firstOrder?.deliveryStatus != OrderDeliveryStatus.NEW.value)*/

            // do collapse or not & adjust peekHeight
            val peekHeight: Int
            if (isCollapsedSheet) {
                peekHeight = 384.dpToPx()
            } else {
                peekHeight = 252.dpToPx()

                // setup mapbox logo padding
                binding.mapView.logo.marginBottom = (24.dpToPx()).toFloat()

                // setup map bottom padding
                binding.mapView.setPadding(
                    0,
                    0,
                    0,
                    164.dpToPx()
                )

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            bottomSheetBehavior.peekHeight = peekHeight

            // enable/disable dragging
            bottomSheetBehavior.isDraggable = isCollapsedSheet

            // draw route
            if (it == null) {
                clearRouteAndStopNavigation()
                clientSymbol = null
                branchSymbol = null
            } else {
                if (viewModel.isNavigationView.value == true) {
                    findNavigationViewRoute()
                } else {
                    addMarkersToMap()
                }
            }
        })

        binding.mapView.location.addOnIndicatorPositionChangedListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                // todo send location to api
                /*if (viewModel.ordersResponse.value == null && viewModel.isAvailable.value == true)
                    viewModel.getPendingOrders(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        lat = it.latitude(),
                        lng = it.longitude()
                    )*/

                // setup location icon
                /*googleMap?.clear() todo does mapBox auto changes location marker?

            val locationMarkerOptions = MarkerOptions()
            locationMarkerOptions.icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_location_pin))
            locationMarkerOptions.position(LatLng(it.latitude, it.longitude))

            googleMap?.addMarker(locationMarkerOptions)

            // animate camera to location
            googleMap!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    DEFAULT_ZOOM
                ), 4000, null
            )*/

            }
        }

        viewModel.isAvailable.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                if (viewModel.availabilityResponse.value?.status != ProjectConstant.Companion.Status.LOADING
                ) {
                    viewModel.changeAvailability(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId
                    ).observe(viewLifecycleOwner, {

                        // show loading
                        if (it.status == ProjectConstant.Companion.Status.LOADING)
                            ProjectDialogUtils.showLoading(requireContext())
                        else {
                            ProjectDialogUtils.hideLoading()
                        }
                        // reset the switch if have error
                        if (it.status == ProjectConstant.Companion.Status.API_ERROR
                            || it.status == ProjectConstant.Companion.Status.NETWORK_ERROR
                        ) {
                            // apply availability to switch
                            viewModel.isAvailable.value = !viewModel.isAvailable.value!!

                            viewModel.isFirstFetchData = true
                        }
                        viewModel.availabilityResponse.value?.data?.isAvailable =
                            viewModel.isAvailable.value
                        getOrRemovePendingOrders()
                    })
                }
                /*if (viewModel.isFirstFetchData) {
                    viewModel.isFirstFetchData = false
                }*/
            }
        })
    }

    private fun findNavigationViewRoute() {
        val it = viewModel.selectedOrder.value

        val clientLat = it?.clientAddress?.latitude?.toDoubleOrNull()
        val clientLng = it?.clientAddress?.longitude?.toDoubleOrNull()

        val branchLat = it?.firstOrder?.branch?.latitude?.toDoubleOrNull()
        val branchLng = it?.firstOrder?.branch?.longitude?.toDoubleOrNull()

        if (branchLat != null && branchLng != null && clientLat != null && clientLng != null) {
            val clientPoint = Point.fromLngLat(clientLng, clientLat)
            val branchPoint = Point.fromLngLat(branchLng, branchLat)

            if (it.firstOrder.isReturn == true)
                findRoute(clientPoint, branchPoint)
            else
                findRoute(branchPoint, clientPoint)
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())

            viewModel.fetchData(
                requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId
            )
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.bottomSheet)
        /*binding.bottomSheet.setOnClickListener {
            bottomSheetBehavior.state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
        }*/

        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val currentHeight: Int = binding.root.height - bottomSheet.height
                val bottomSheetShiftDown = currentHeight - bottomSheet.top

                binding.mapView.logo.marginBottom =
                    (binding.bottomSheet.height + bottomSheetShiftDown - 110.dpToPx() - 204.dpToPx() - 26.dpToPx()).toFloat()
                /*googleMap?.setPadding(
                    0,
                    24.dpToPx(),
                    0, todo
                    binding.bottomSheet.height + bottomSheetShiftDown - 42.dpToPx()
                )*/
                /*if (viewModel.location.value != null) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(
                            LatLng(
                                viewModel.location.value!!.latitude,
                                viewModel.location.value!!.longitude
                            )
                        ) // Sets the center of the map to location user
                        //.zoom(DEFAULT_ZOOM) // Sets the zoom todo
                        .bearing(90f) // Sets the orientation of the camera to east
                        .tilt(40f) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder
                    /*googleMap?.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition
                        ) todo
                    )*/
                }*/
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })
    }

    private fun setupOnClick() {
        binding.navigationFab.setOnClickListener {
            viewModel.isNavigationView.value = !viewModel.isNavigationView.value!!
        }

        binding.screenClickableIv.setOnClickListener {
            closeSideNavigationMenu()
        }

        binding.upButtonIv.setOnClickListener {
            viewModel.selectedOrder.value = null
        }

        binding.navMenuIv.setOnClickListener {
            if (viewModel.isSideNavMenuOpened.value == true)
                closeSideNavigationMenu()
            else
                openSideNavigationMenu()
        }

        binding.positiveBtn.setOnClickListener {
            // in case action button is "Done"
            if (viewModel.selectedOrder.value?.firstOrder?.deliveryStatusEnum == OrderDeliveryStatus.DELIVERED) {
                viewModel.selectedOrder.value = null

                // clear markers
                mapboxSdkMapView.style?.removeImage(CLIENT_ICON_ID)
                mapboxSdkMapView.style?.removeImage(BRANCH_ICON_ID)

                return@setOnClickListener
            }

            val nextStatus =
                when (viewModel.selectedOrder.value?.firstOrder?.deliveryStatusEnum) {
                    OrderDeliveryStatus.NEW -> OrderDeliveryStatus.ACCEPTED.value
                    OrderDeliveryStatus.ACCEPTED -> OrderDeliveryStatus.STARTED.value
                    OrderDeliveryStatus.STARTED -> OrderDeliveryStatus.PICKED.value
                    OrderDeliveryStatus.PICKED -> OrderDeliveryStatus.ARRIVED.value
                    OrderDeliveryStatus.ARRIVED -> OrderDeliveryStatus.DELIVERED.value
                    else -> null
                }

            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.changeOrderStatus(
                    langTag = requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    orderId = viewModel.selectedOrder.value?.firstOrder?.id,
                    status = nextStatus,
                    isReturn = viewModel.selectedOrder.value?.firstOrder?.isReturn
                ).observeApiResponse(this@HomeFragment, {
                    viewModel.ordersPagedList?.value?.dataSource?.invalidate()
                    viewModel.selectedOrder.value = viewModel.selectedOrder.value?.also {
                        it.firstOrder?.deliveryStatus = nextStatus
                    }
                })
            }
        }

        binding.rejectBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.changeOrderStatus(
                    langTag = requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    orderId = viewModel.selectedOrder.value?.firstOrder?.id,
                    status = OrderDeliveryStatus.REJECTED.value,
                    isReturn = viewModel.selectedOrder.value?.firstOrder?.isReturn
                ).observeApiResponse(this@HomeFragment, {
                    viewModel.ordersPagedList?.value?.dataSource?.invalidate()
                    viewModel.selectedOrder.value = null
                })
            }
        }
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

    private fun getOrRemovePendingOrders() {
        if (viewModel.location.value != null) {
            if (viewModel.isAvailable.value == true) {
                viewModel.ordersPagedList?.value?.dataSource?.invalidate()
            } else
                viewModel.ordersPagedList?.value?.dataSource?.invalidate()
            //viewModel.ordersResponse.value = null
        }
    }

    fun Context.startUploadingLocation() {
        if (checkLocationPermissions().not()) return

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<LocationUploadWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
            //.addTag("LocationWorkTag")
            .build()

        WorkManager
            .getInstance(this)
            .enqueue(request)
    }

    private fun Context.checkLocationPermissions() =
        ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
        Toast.makeText(
            requireContext(),
            R.string.user_location_permission_explanation,
            Toast.LENGTH_LONG
        )
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted && ::mapboxSdkMapView.isInitialized) {
            val style: com.mapbox.mapboxsdk.maps.Style? = mapboxSdkMapView.style
            if (style != null) {
                enableLocationPlugin(style)
            }
        } else {
            Toast.makeText(
                requireContext(),
                R.string.user_location_permission_not_granted,
                Toast.LENGTH_LONG
            )
                .show()
            findNavController().popBackStack()
        }
    }

}