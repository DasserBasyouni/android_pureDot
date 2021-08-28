package com.g7.soft.pureDot.ui.screen.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.g7.soft.pureDot.adapter.PendingOrdersAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.SideNavMenuAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.g7.soft.pureDot.utils.MapBoxUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.gestures
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
import java.util.*


open class HomeFragment : Fragment() {

    internal lateinit var binding: FragmentHomeBinding
    internal lateinit var viewModel: HomeViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    /* ----- Mapbox Maps components ----- */
    private lateinit var mapboxMap: MapboxMap

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
                    Toast.makeText(
                        requireActivity(),
                        error.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    binding.maneuverView.visibility = View.VISIBLE
                    binding.maneuverView.renderManeuvers(maneuvers)
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

        mapboxMap = binding.mapView.getMapboxMap()

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSideNavigationMenu()
        setupObservables()
        fetchData()
        setupBottomSheet()
        setupOnClick()

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

        // initialize Mapbox Navigation
        mapboxNavigation = MapboxNavigation(
            NavigationOptions.Builder(requireContext())
                .accessToken(getMapboxAccessTokenFromResources())
                .build()
        )
        // setup map ui
        //binding.mapView.get.
        binding.mapView.logo.marginBottom = (384.dpToPx() - 110.dpToPx()).toFloat()

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
        viewportDataSource = MapboxNavigationViewportDataSource(
            binding.mapView.getMapboxMap()
        )
        navigationCamera = NavigationCamera(
            binding.mapView.getMapboxMap(),
            binding.mapView.camera,
            viewportDataSource
        )
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState -> // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> binding.recenter.visibility =
                    View.INVISIBLE

                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
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
            binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(point)
                true
            }
        }

        // initialize view interactions
        binding.stop.setOnClickListener {
            clearRouteAndStopNavigation()
        }
        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
        }
        binding.routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            binding.recenter.showTextAndExtend(2000L)
        }
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

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        mapboxNavigation.onDestroy()
        speechAPI.cancel()
        voiceInstructionsPlayer.shutdown()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
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


    private fun findRoute(destination: Point) {
        val origin = navigationLocationProvider.lastLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(requireContext())
                //.accessToken(getMapboxAccessTokenFromResources())
                .coordinatesList(listOf(origin, destination))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes.first(), routerOrigin)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                    // no impl
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    // no impl
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(route: DirectionsRoute, routerOrigin: RouterOrigin) {
        // set route
        mapboxNavigation.setRoutes(listOf(route))

        // show UI elements
        binding.soundButton.visibility = View.VISIBLE
        binding.routeOverview.visibility = View.VISIBLE
        binding.tripProgressCard.visibility = View.VISIBLE
        binding.routeOverview.showTextAndExtend(2000L)
        binding.soundButton.unmuteAndExtend(2000L)

        // move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()
    }

    private fun clearRouteAndStopNavigation() {
        // clear
        mapboxNavigation.setRoutes(listOf())

        // hide UI elements
        binding.soundButton.visibility = View.INVISIBLE
        binding.maneuverView.visibility = View.INVISIBLE
        binding.routeOverview.visibility = View.INVISIBLE
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
            viewModel.userDataLcee.value!!.response.value = it
            sideNavMenuAdapter.updateWallet(it.data?.balance, it.data?.currency)
        })
    }

    private fun setupObservables() {
        viewModel.availabilityResponse.observe(viewLifecycleOwner, {
            viewModel.availabilityLcee.value!!.response.value = it
            binding.isAvailableS.isChecked = it.data?.isAvailable ?: false
        })

        val ordersAdapter = PendingOrdersAdapter(this)
        binding.ordersRv.adapter = ordersAdapter
        viewModel.ordersResponse.observe(viewLifecycleOwner, {
            viewModel.ordersLcee.value!!.response.value = it
            ordersAdapter.submitList(it?.data)
        })

        viewModel.selectedOrder.observe(viewLifecycleOwner, {
            binding.productsRv.adapter = ProductsAdapter().apply { this.submitList(it?.products) }
            Log.e("Z_", "status: ${it?.deliveryStatus}")
            val isCollapsedSheet =
                !(it != null && it.deliveryStatus != ApiConstant.OrderDeliveryStatus.ACCEPTED.value
                        && it.deliveryStatus != ApiConstant.OrderDeliveryStatus.NEW.value)

            // do collapse or not & adjust peekHeight
            val peekHeight: Int
            if (isCollapsedSheet) {
                peekHeight = 384.dpToPx()
            } else {
                peekHeight = 432.dpToPx()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            bottomSheetBehavior.peekHeight = peekHeight

            // enable/disable dragging
            bottomSheetBehavior.isDraggable = isCollapsedSheet

        })

        binding.mapView.location.addOnIndicatorPositionChangedListener {
            lifecycleScope.launch {
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

                if (viewModel.ordersResponse.value == null && viewModel.isAvailable.value == true)
                    viewModel.getPendingOrders(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        lat = it.latitude(),
                        lng = it.longitude()
                    )

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
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

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
            }
        })
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val tokenId =
                ClientRepository("").getLocalUserData(requireContext()).tokenId

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
                    (binding.bottomSheet.height + bottomSheetShiftDown - 110.dpToPx()).toFloat()
                /*googleMap?.setPadding(
                    0,
                    24.dpToPx(),
                    0, todo
                    binding.bottomSheet.height + bottomSheetShiftDown - 42.dpToPx()
                )*/
                if (viewModel.location.value != null) {
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
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })
    }

    private fun setupOnClick() {
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
            if (viewModel.selectedOrder.value?.deliveryStatusEnum == ApiConstant.OrderDeliveryStatus.DELIVERED) {
                viewModel.selectedOrder.value = null
                return@setOnClickListener
            }

            val nextStatus =
                when (viewModel.selectedOrder.value?.deliveryStatusEnum) {
                    ApiConstant.OrderDeliveryStatus.NEW -> ApiConstant.OrderDeliveryStatus.ACCEPTED.value
                    ApiConstant.OrderDeliveryStatus.ACCEPTED -> ApiConstant.OrderDeliveryStatus.STARTED.value
                    ApiConstant.OrderDeliveryStatus.STARTED -> ApiConstant.OrderDeliveryStatus.PICKED.value
                    ApiConstant.OrderDeliveryStatus.PICKED -> ApiConstant.OrderDeliveryStatus.ARRIVED.value
                    ApiConstant.OrderDeliveryStatus.ARRIVED -> ApiConstant.OrderDeliveryStatus.DELIVERED.value
                    else -> null
                }

            lifecycleScope.launch {
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

                viewModel.changeOrderStatus(
                    langTag = requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    orderNumber = viewModel.selectedOrder.value?.number,
                    status = nextStatus
                ).observeApiResponse(this, {
                    viewModel.ordersResponse.value =
                        viewModel.ordersResponse.value.also {
                            it?.data?.firstOrNull { order ->
                                order.number == viewModel.selectedOrder.value?.number
                            }?.deliveryStatus = nextStatus
                        }
                    viewModel.selectedOrder.value = viewModel.selectedOrder.value?.also {
                        it.deliveryStatus = nextStatus
                    }
                })
            }
        }

        binding.rejectBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

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

    private fun getOrRemovePendingOrders(tokenId: String) {
        if (viewModel.location.value != null) {
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
}