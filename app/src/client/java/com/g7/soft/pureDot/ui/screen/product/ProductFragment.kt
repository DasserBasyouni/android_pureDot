package com.g7.soft.pureDot.ui.screen.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.g7.soft.pureDot.databinding.FragmentProductBinding
import com.g7.soft.pureDot.ext.observeApiResponse
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
        val similarProductsAdapter = ProductsAdapter(this, isGrid = false)
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
                "itemId" to args.item.id
            )
            findNavController().navigate(R.id.allReviewsFragment, bundle)
        }
        binding.addToCartBtn.setOnClickListener {
            val newQuantity = binding.quantityInCartTv.text.toString().toIntOrNull()?.plus(1)
            viewModel.editCartQuantity(
                requireActivity().currentLocale.toLanguageTag(),
                itemId = args.item.id,
                quantity = newQuantity
            ).observeApiResponse(this, {
                viewModel.product?.quantityInCart = newQuantity
                binding.quantityInCartTv.text = newQuantity.toString()
                ProjectDialogUtils.showCheckoutTopPopup(
                    requireContext(),
                    itemName = viewModel.product?.name,
                    totalPriceInCart = it?.totalPriceInCart,
                    currency = viewModel.product?.currency,
                    positiveBtnOnClick = {
                        findNavController().navigate(R.id.cartFragment)
                    }
                )
            })
        }
    }
}