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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ImagesSliderAdapter
import com.g7.soft.pureDot.adapter.ReviewsAdapter
import com.g7.soft.pureDot.adapter.ServiceVariantsAdapter
import com.g7.soft.pureDot.adapter.SmallServicesAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentServiceBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ServiceDetailsModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            val isGuestAccount = UserRepository("").getIsGuestAccount(requireContext())
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            binding.currency = currencySymbol
            binding.isGuestAccount = isGuestAccount
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@ServiceFragment
        }

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
            setupBranchSpinner(
                binding.branchesSpinner,
                it,
                initialText = getString(R.string.select_branch)
            )
        })

        // setup date picker todo
        binding.calendarView.minDate = Calendar.getInstance().timeInMillis

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.fetchData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        val imagesOffersAdapter = ImagesSliderAdapter(this)
        val similarServicesAdapter = SmallServicesAdapter(this)
        val reviewsAdapter = ReviewsAdapter(this)
        binding.sliderOffersLceeLayoutVp.adapter = imagesOffersAdapter
        binding.similarProductsRv.adapter = similarServicesAdapter
        binding.reviewsRv.adapter = reviewsAdapter
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl,
            binding.sliderOffersLceeLayoutVp
        ) { _, _ -> }.attach()
        viewModel.serviceDetailsResponse.observe(viewLifecycleOwner, {
            viewModel.serviceDetailsLcee.value!!.response.value = it
            imagesOffersAdapter.submitList(it.data?.images)
            similarServicesAdapter.submitList(it.data?.similarServices)
            reviewsAdapter.submitList(it.data?.reviews?.data)
            binding.variationsRv.adapter = ServiceVariantsAdapter(it.data?.variations, viewModel)
        })
        viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp.currentItem = it
        })
        viewModel.selectedVariations.observe(viewLifecycleOwner, {
            viewModel.getCost(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.costResponse.observe(viewLifecycleOwner, {
            viewModel.costLcee.value!!.response.value = it
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
                "itemId" to args.service.id,
                "isProduct" to false
            )
            findNavController().navigate(R.id.allReviewsFragment, bundle)
        }
        binding.sendBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.addReview(requireActivity().currentLocale.toLanguageTag(), tokenId)
                    .observeApiResponse(this@ServiceFragment, {
                        viewModel.serviceDetailsResponse.value?.data?.userReview = it
                        binding.invalidateAll()
                    })
            }
        }
        binding.decreaseCartQuantityBtn.setOnClickListener {
            if (viewModel.quantityInCart.value != 1)
                viewModel.quantityInCart.value = viewModel.quantityInCart.value?.minus(1)
        }
        binding.increaseCartQuantityBtn.setOnClickListener {
            viewModel.quantityInCart.value = viewModel.quantityInCart.value?.plus(1)
        }
        binding.addToCartBtn.setOnClickListener {
            if (viewModel.selectedBranch == null)
                ProjectDialogUtils.showSimpleMessage(
                    requireContext(),
                    R.string.branch_is_required,
                    R.drawable.ic_secure_shield
                )
            else if (viewModel.serviceDetailsResponse.value?.data?.variations != null
                && viewModel.selectedVariations.value?.size
                != viewModel.serviceDetailsResponse.value?.data?.variations?.size
            )
                ProjectDialogUtils.showSimpleMessage(
                    requireContext(),
                    R.string.variations_are_required,
                    R.drawable.ic_secure_shield
                )
            else
                lifecycleScope.launch {
                    val isGuestAccount = UserRepository("").getIsGuestAccount(requireContext())
                    val tokenId = UserRepository("").getTokenId(requireContext())

                    if (isGuestAccount)
                        findNavController().navigate(R.id.loginFragment)
                    else {
                        viewModel.checkCartItems(
                            requireActivity().currentLocale.toLanguageTag(),
                            tokenId = tokenId
                        ).observeApiResponse(this@ServiceFragment, {
                            val bundle = bundleOf(
                                "masterOrder" to it,
                                "serviceBranchId" to viewModel.selectedBranch?.id,
                                "serviceVariations" to viewModel.selectedVariations.value?.toTypedArray(),
                                "serviceId" to viewModel.service?.id,
                                "serviceShopId" to viewModel.service?.shop?.id,
                                "serviceQuantity" to viewModel.quantityInCart.value
                            )
                            findNavController().navigate(R.id.checkoutFragment, bundle)
                        })
                    }
                }
        }
    }


    private fun setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<ServiceDetailsModel>?,
        initialText: String
    ) {
        val spinnerData = when (networkResponse?.status) {
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


    private fun setupBranchSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<ServiceDetailsModel>?,
        initialText: String
    ) {
        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS -> {
                val modelsList = networkResponse.data?.branches
                val dataList = modelsList?.mapNotNull { it.name }?.toTypedArray()
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
            spinner.setSelection(viewModel.selectedBranchPosition.value!!)
        }
    }
}