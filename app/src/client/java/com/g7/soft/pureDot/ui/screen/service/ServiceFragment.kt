package com.g7.soft.pureDot.ui.screen.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ImagesSliderAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.ReviewsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentServiceBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ServiceDetailsModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import java.util.*

class ServiceFragment : Fragment() {
    private lateinit var binding: FragmentServiceBinding
    private lateinit var viewModelFactory: ServiceViewModelFactory
    internal lateinit var viewModel: ServiceViewModel
    private val args: ServiceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_service, container, false)

        viewModelFactory = ServiceViewModelFactory(
            service = args.service,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ServiceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.service.name

        // setup spinner
        viewModel.serviceDetailsResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.servantsSpinner,
                viewModel.serviceDetailsResponse.value,
                initialText = getString(R.string.servant)
            )
        })

        // setup date picker todo
        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        val imagesOffersAdapter = ImagesSliderAdapter(this)
        val similarProductsAdapter = ProductsAdapter(this, isGrid = false)
        val reviewsAdapter = ReviewsAdapter(this)
        binding.sliderOffersLceeLayoutVp.adapter = imagesOffersAdapter
        binding.similarProductsRv.adapter = similarProductsAdapter
        binding.reviewsRv.adapter = reviewsAdapter
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl,
            binding.sliderOffersLceeLayoutVp
        ) { _, _ -> }.attach()
        viewModel.serviceDetailsResponse.observe(viewLifecycleOwner, {
            viewModel.serviceDetailsLcee.value!!.response.value = it
            imagesOffersAdapter.submitList(it.data?.images)
            similarProductsAdapter.submitList(it.data?.similarItems)
            reviewsAdapter.submitList(it.data?.reviews?.data)
        })
        viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp.currentItem = it
        })

        // add decoration divider
        binding.reviewsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.reviewsSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "itemId" to args.service.id
            )
            findNavController().navigate(R.id.allReviewsFragment, bundle)
        }
        binding.addToCartBtn.setOnClickListener {
            val newQuantity = binding.quantityInCartTv.text.toString().toIntOrNull()?.plus(1)
            viewModel.editCartQuantity(
                requireActivity().currentLocale.toLanguageTag(),
                itemId = args.service.id,
                quantity = newQuantity
            ).observeApiResponse(this, {
                viewModel.service?.quantityInCart = newQuantity
                binding.quantityInCartTv.text = newQuantity.toString()
                ProjectDialogUtils.showCheckoutTopPopup(
                    requireContext(),
                    itemName = viewModel.service?.name,
                    totalPriceInCart = it?.totalPriceInCart,
                    currency = viewModel.service?.items?.products?.first()?.currency,
                    positiveBtnOnClick = {
                        findNavController().navigate(R.id.cartFragment)
                    }
                )
            })
        }
    }


    private fun setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<ServiceDetailsModel>?,
        initialText: String
    ) {
        val spinnerData = when (networkResponse?.status)     {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS -> {
                val dataList = mutableListOf<String>()

                for (i in 1..(networkResponse.data?.maxServants ?: 0))
                    dataList.add("$i")

                spinner.isEnabled = true
                arrayListOf(initialText).apply {
                    this.addAll(dataList.toTypedArray())
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
            spinner.setSelection(viewModel.servantsSelectedPosition.value!!)
        }
    }
}