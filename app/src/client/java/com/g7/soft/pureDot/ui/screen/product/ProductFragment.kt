package com.g7.soft.pureDot.ui.screen.product

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
import com.g7.soft.pureDot.adapter.ProductVariantsAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.ReviewsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentProductBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductDetailsModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var viewModelFactory: ProductViewModelFactory
    internal lateinit var viewModel: ProductViewModel
    private val args: ProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_product, container, false)

        viewModelFactory = ProductViewModelFactory(
            product = args.item,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.item.name

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        val imagesOffersAdapter = ImagesSliderAdapter(this)
        val similarProductsAdapter = ProductsAdapter(this, isGrid = false, editWishList = this::editWishList)
        val reviewsAdapter = ReviewsAdapter(this)
        binding.sliderOffersLceeLayoutVp.adapter = imagesOffersAdapter
        binding.similarProductsRv.adapter = similarProductsAdapter
        binding.reviewsRv.adapter = reviewsAdapter
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl,
            binding.sliderOffersLceeLayoutVp
        ) { _, _ -> }.attach()
        viewModel.productDetailsResponse.observe(viewLifecycleOwner, {
            viewModel.productDetailsLcee.value!!.response.value = it
            imagesOffersAdapter.submitList(it.data?.images)
            similarProductsAdapter.submitList(it.data?.similarItems)
            reviewsAdapter.submitList(it.data?.reviews?.data)
            binding.variationsRv.adapter = ProductVariantsAdapter(it.data?.variations)
            setupSpinner(
                binding.branchesSpinner,
                it,
                initialText = getString(R.string.select_branch)
            )
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
        binding.wishListCiv.setOnClickListener {
            val tokenId = "" //todo
            editWishList(
                tokenId,
                viewModel.product?.id,
                binding.wishListCiv.isChecked
            ) {
                val isChecked = !binding.wishListCiv.isChecked
                args.item.isInWishList = isChecked
                viewModel.product?.isInWishList = isChecked
                binding.wishListCiv.isChecked = isChecked
            }
        }
        binding.reviewsSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "itemId" to args.item.id
            )
            findNavController().navigate(R.id.allReviewsFragment, bundle)
        }
        binding.sendBtn.setOnClickListener {
            val tokenId = "" // todo

            viewModel.addReview(requireActivity().currentLocale.toLanguageTag(), tokenId)
                .observeApiResponse(this, {
                    viewModel.product?.userReview = it
                    binding.invalidateAll()
                })
        }
        binding.decreaseCartQuantityBtn.setOnClickListener {
            if (viewModel.quantityInCart.value != 1)
                viewModel.quantityInCart.value = viewModel.quantityInCart.value?.minus(1)
        }
        binding.increaseCartQuantityBtn.setOnClickListener {
            viewModel.quantityInCart.value = viewModel.quantityInCart.value?.plus(1)
        }
        binding.addToCartBtn.setOnClickListener {
            viewModel.addProductToCart(
                requireActivity().currentLocale.toLanguageTag(),
                requireContext()
            ) {
                viewModel.quantityInCart.value = 1
                viewModel.getTotalProductsPriceInCart(
                    requireActivity().currentLocale.toLanguageTag(),
                    requireContext(),
                    onComplete = { totalPrice ->
                        ProjectDialogUtils.showCheckoutTopPopup(
                            requireContext(),
                            itemName = viewModel.product?.name,
                            totalPriceInCart = totalPrice,
                            currency = viewModel.product?.currency,
                            positiveBtnOnClick = {
                                findNavController().navigate(R.id.cartFragment)
                            }
                        )
                    }
                )

            }
        }
    }


    private fun setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<ProductDetailsModel>?,
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

    private fun editWishList(
        tokenId: String,
        productId: Int?,
        doAdd: Boolean,
        onComplete: () -> Unit
    ) {
        viewModel.editWishList(
            requireActivity().currentLocale.toLanguageTag(),
            tokenId = tokenId,
            productId = productId,
            doAdd = doAdd
        ).observeApiResponse(this, { onComplete.invoke() })
    }
}