package com.g7.soft.pureDot.ui.screen.address


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.BUNDLE_ADDRESS
import com.g7.soft.pureDot.databinding.FragmentAddressBinding
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.AddressModel
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.MapBoxUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.core.exceptions.ServicesException
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.Property.NONE
import com.mapbox.mapboxsdk.style.layers.Property.VISIBLE
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.fragment_address.view.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


open class AddressFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var viewModelFactory: AddressViewModelFactory
    internal lateinit var viewModel: AddressViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val args: AddressFragmentArgs by navArgs()

    // picker
    private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    private var mapView: MapView? = null
    private lateinit var mapboxMap: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // setup map instance
        Mapbox.getInstance(requireContext(), MapBoxUtils.getMapboxAccessToken(requireContext()))

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_address,
            container,
            false
        )

        viewModelFactory = AddressViewModelFactory(userData = args.userData)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getCounties(requireActivity().currentLocale.toLanguageTag())

        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.selectedCityPosition.observe(viewLifecycleOwner, {
            viewModel.getZipCodes(requireActivity().currentLocale.toLanguageTag())
        })

        // observables
        viewModel.countiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.countriesSpinner,
                viewModel.countiesResponse.value,
                initialText = getString(R.string.select_country)
            )
        })
        viewModel.citiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.citiesSpinner,
                viewModel.citiesResponse.value,
                initialText = getString(R.string.select_city)
            )
        })
        viewModel.zipCodesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.zipCodeSpinner,
                viewModel.zipCodesResponse.value,
                initialText = getString(R.string.select_zipcode)
            )
        })

        // setup click listener
        binding.navMenuIv.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.addAddress(
                    langTag = requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    latitude = viewModel.locationLatLng?.value?.latitude,
                    longitude = viewModel.locationLatLng?.value?.longitude
                ).observeApiResponse(this@AddressFragment, {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        ProjectConstant.RESULTS_ADD_ADDRESS, bundleOf(
                            BUNDLE_ADDRESS to AddressModel(
                                id = it,
                                flat = viewModel.flat.value,
                                floor = viewModel.floor.value,
                                buildingNumber = viewModel.buildingNumber.value,
                                streetName = viewModel.streetName.value,
                                areaName = viewModel.areaName.value,
                                isMainAddress = viewModel.isMainAddress.value,
                                cityId = viewModel.selectedCity?.id,
                                latitude = viewModel.locationLatLng?.value?.latitude?.toString(),
                                longitude = viewModel.locationLatLng?.value?.longitude?.toString(),
                                zipCodeId = viewModel.selectedZipCode?.id,
                                cityName = viewModel.selectedCity?.name
                            )
                        )
                    )

                    findNavController().navigateUp()
                }, validationObserve = {
                    binding.areaTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_AREA)
                            getString(R.string.error_empty_area) else null

                    binding.streetNameTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_STREET_NAME)
                            getString(R.string.error_empty_street_name) else null

                    binding.flatTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_FLAT)
                            getString(R.string.error_empty_flat) else null

                    binding.floorTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_FLOOR)
                            getString(R.string.error_empty_floor) else null

                    binding.buildingNumberTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_BUILDING_NUMBER)
                            getString(R.string.error_empty_building_number) else null
                })
            }
        }
        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }

        // setup bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.root.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val currentHeight: Int = binding.root.height - bottomSheet.height
                val bottomSheetShiftDown = currentHeight - bottomSheet.top

                mapboxMap.uiSettings.setLogoMargins(
                    8.dpToPx(),
                    0,
                    8.dpToPx(),
                    binding.bottomSheet.height + bottomSheetShiftDown + 8.dpToPx() - 64.dpToPx()
                )

                /*if (viewModel.location?.value != null) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(
                            LatLng(
                                viewModel.location!!.value!!.latitude,
                                viewModel.location!!.value!!.longitude
                            )
                        ) // Sets the center of the map to location user
                        .zoom(17.0) // Sets the zoom
                        .bearing(90.0) // Sets the orientation of the camera to east
                        .tilt(40.0) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder
                    /*googleMap?.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition
                        )
                    )*/ // todo re position map center
                }*/
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })

        // setup picker
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this@AddressFragment.mapboxMap = mapboxMap

        mapboxMap.uiSettings.setLogoMargins(
            8.dpToPx(),
            0,
            8.dpToPx(),
            110.dpToPx() + 8.dpToPx()
        )

        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { style ->
            enableLocationPlugin(style)

            // Toast instructing user to tap on the mapboxMap
            Toast.makeText(
                requireContext(), getString(R.string.move_map_instruction), Toast.LENGTH_SHORT
            ).show()

            // When user is still picking a location, we hover a marker above the mapboxMap in the center.
            // This is done by using an image view with the default marker found in the SDK. You can
            // swap out for your own marker image, just make sure it matches up with the dropped marker.
            hoveringMarker = ImageView(requireContext())
            hoveringMarker?.setImageResource(R.drawable.ic_pin)
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            )
            hoveringMarker?.layoutParams = params
            mapView!!.addView(hoveringMarker)

            // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
            initDroppedMarker(style)

            // Button for user to drop marker or to pick marker back up.
            binding.selectLocationButton.setOnClickListener {
                if (hoveringMarker?.visibility == View.VISIBLE) {
                    // Use the map target's coordinates to make a reverse geocoding search
                    val mapTargetLatLng = mapboxMap.cameraPosition.target

                    viewModel.locationLatLng?.value =
                        LatLng(mapTargetLatLng.latitude, mapTargetLatLng.longitude)

                    // Hide the hovering red hovering ImageView marker
                    hoveringMarker?.visibility = View.INVISIBLE

                    // Transform the appearance of the button to become the cancel button
                    binding.selectLocationButton.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.driverRed)
                    )
                    binding.selectLocationButton.text =
                        getString(R.string.location_picker_select_location_button_cancel)

                    // Show the SymbolLayer icon to represent the selected map location
                    if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                        val source =
                            style.getSourceAs<GeoJsonSource>("dropped-marker-source-id")
                        source?.setGeoJson(
                            Point.fromLngLat(
                                mapTargetLatLng.longitude,
                                mapTargetLatLng.latitude
                            )
                        )
                        droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID)
                        if (droppedMarkerLayer != null) {
                            droppedMarkerLayer!!.setProperties(visibility(VISIBLE))
                        }
                    }

                    // Use the map camera target's coordinates to make a reverse geocoding search
                    reverseGeocode(
                        Point.fromLngLat(
                            mapTargetLatLng.longitude,
                            mapTargetLatLng.latitude
                        )
                    )
                } else {
                    viewModel.locationLatLng?.value = null

                    // Switch the button appearance back to select a location.
                    binding.selectLocationButton.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.warm_purple)
                    )
                    binding.selectLocationButton.text =
                        getString(R.string.location_picker_select_location_button_select)

                    // Show the red hovering ImageView marker
                    hoveringMarker?.visibility = View.VISIBLE

                    // Hide the selected location SymbolLayer
                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID)
                    if (droppedMarkerLayer != null) {
                        droppedMarkerLayer?.setProperties(visibility(NONE))
                    }
                }
            }
        }
    }


    private fun <T> setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<T>?>?,
        initialText: String
    ) {
        var selectedPosition = 0

        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS, ProjectConstant.Companion.Status.API_ERROR -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        selectedPosition = viewModel.selectedCountryPosition.value!!
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        selectedPosition = viewModel.selectedCityPosition.value!!
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

                    }
                    modelsList?.firstOrNull() is ZipCodeModel -> {
                        selectedPosition = viewModel.selectedZipCodePosition.value!!
                        modelsList.mapNotNull { (it as ZipCodeModel).code }.toTypedArray()
                    }
                    else -> null
                }
                spinner.isEnabled = true
                arrayListOf(initialText).apply {
                    this.addAll((dataList ?: arrayOf()))
                }
            }
            else -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.something_went_wrong))
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(selectedPosition)
        }
    }

    private fun initDroppedMarker(@NonNull loadedMapStyle: Style) {
        // Add the marker image to map
        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_pin, null)
        val mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)
        loadedMapStyle.addImage(
            "dropped-icon-image", mBitmap!!
        )
        loadedMapStyle.addSource(GeoJsonSource("dropped-marker-source-id"))
        loadedMapStyle.addLayer(
            SymbolLayer(
                DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id"
            ).withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
            )
        )
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
        Toast.makeText(
            requireContext(),
            R.string.user_location_permission_explanation,
            Toast.LENGTH_LONG
        )
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted && mapboxMap != null) {
            val style: Style? = mapboxMap.style
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

    /**
     * This method is used to reverse geocode where the user has dropped the marker.
     *
     * @param point The location to use for the search
     */
    private fun reverseGeocode(point: Point) {
        try {
            val client: MapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(MapBoxUtils.getMapboxAccessToken(requireContext()))
                .query(Point.fromLngLat(point.longitude(), point.latitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build()
            client.enqueueCall(object : Callback<GeocodingResponse?> {
                override fun onResponse(
                    call: Call<GeocodingResponse?>?,
                    response: Response<GeocodingResponse?>
                ) {
                    if (response.body() != null) {
                        val results: List<CarmenFeature>? = response.body()?.features()
                        if (!results.isNullOrEmpty()) {
                            val feature = results[0]

                            // If the geocoder returns a result, we take the first in the list and show a Toast with the place name.
                            mapboxMap.getStyle { style ->
                                if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                    viewModel.areaName.value = feature.placeName()
                                    viewModel.streetName.value = feature.address()
                                    /*Toast.makeText(
                                        requireContext(), String.format(
                                            getString(R.string.location_picker_place_name_result),
                                            feature.placeName()
                                        ), Toast.LENGTH_SHORT
                                    ).show()*/
                                }
                            }
                        } else {
                            /*Toast.makeText(
                                requireContext(),
                                getString(R.string.location_picker_dropped_marker_snippet_no_results),
                                Toast.LENGTH_SHORT
                            ).show()*/
                        }
                    }
                }

                override fun onFailure(call: Call<GeocodingResponse?>?, throwable: Throwable) {
                    Timber.e("Geocoding Failure: %s", throwable.message)
                }
            })
        } catch (servicesException: ServicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString())
            servicesException.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationPlugin(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            val locationComponent: LocationComponent = mapboxMap.locationComponent
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
}