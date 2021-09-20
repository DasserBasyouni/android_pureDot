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
import androidx.lifecycle.lifecycleScope
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
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import kotlinx.coroutines.launch

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
            productId = args.productId
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        lifecycleScope.launch {
            val isGuestAccount = UserRepository("").getIsGuestAccount(requireContext())
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            binding.currency = currencySymbol
            binding.isGuestAccount = isGuestAccount
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@ProductFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.item.name

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.fetchData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        val imagesOffersAdapter = ImagesSliderAdapter(this)
        val similarProductsAdapter =
            ProductsAdapter(this, isGrid = false, editWishList = this::editWishList)
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
            similarProductsAdapter.submitList(it.data?.similarProducts)
            reviewsAdapter.submitList(it.data?.reviews?.data)
            binding.variationsRv.adapter = ProductVariantsAdapter(it.data?.variations, viewModel)
            setupSpinner(
                binding.branchesSpinner,
                it,
                initialText = getString(R.string.select_branch)
            )

            // get price in case of no variations
            /*if (it.data?.variations.isNullOrEmpty())
                viewModel.getCost(requireActivity().currentLocale.toLanguageTag())*/
        })
        viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp.currentItem = it
        })
        viewModel.selectedVariationsIds.observe(viewLifecycleOwner, {
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
        binding.wishListCiv.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

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
        }
        binding.reviewsSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "itemId" to args.item.id,
                "isProduct" to true
            )
            findNavController().navigate(R.id.allReviewsFragment, bundle)
        }
        binding.sendBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.addReview(requireActivity().currentLocale.toLanguageTag(), tokenId)
                    .observeApiResponse(this@ProductFragment, {
                        viewModel.productDetailsResponse.value?.data?.userReview = it
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
            else if (viewModel.productDetailsResponse.value?.data?.variations != null
                && viewModel.selectedVariationsIds.value?.size
                != viewModel.productDetailsResponse.value?.data?.variations?.size
            )
                ProjectDialogUtils.showSimpleMessage(
                    requireContext(),
                    R.string.variations_are_required,
                    R.drawable.ic_secure_shield
                )
            else
                lifecycleScope.launch {
                    val isGuestAccount =
                        UserRepository("").getIsGuestAccount(requireContext())
                    val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

                    if (isGuestAccount)
                        findNavController().navigate(R.id.loginFragment)
                    else
                        viewModel.addProductToCart(
                            requireActivity().currentLocale.toLanguageTag(),
                            requireContext()
                        ) {
                            binding.currency = currencySymbol
                            viewModel.quantityInCart.value = 1
                            viewModel.getTotalProductsPriceInCart(
                                requireActivity().currentLocale.toLanguageTag(),
                                requireContext(),
                                onComplete = { totalPrice ->
                                    ProjectDialogUtils.showCheckoutTopPopup(
                                        requireContext(),
                                        itemName = viewModel.product?.name,
                                        totalPriceInCart = totalPrice,
                                        currency = currencySymbol,
                                        positiveBtnOnClick = {
                                            findNavController().navigate(R.id.cartFragment)
                                        }
                                    )
                                }
                            )

                        }
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
        tokenId: String?,
        productId: String?,
        doAdd: Boolean,
        onComplete: () -> Unit
    ) {
        lifecycleScope.launch {
            val isGuestAccount =
                UserRepository("").getIsGuestAccount(requireContext())

            if (isGuestAccount)
                findNavController().navigate(R.id.loginFragment)
            else
                viewModel.editWishList(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    productId = productId,
                    doAdd = doAdd
                ).observeApiResponse(this@ProductFragment, { onComplete.invoke() })
        }
    }
}