package com.g7.soft.pureDot.ui.screen.seeAll.products

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OffersSliderAdapter
import com.g7.soft.pureDot.adapter.PagedProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllProductsBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*

// todo fix title of screens that appears in other screens, maybe that fix is in navigation also 3 title for this screen
class AllProductsFragment : Fragment() {
    private lateinit var binding: FragmentAllProductsBinding
    private lateinit var viewModelFactory: AllProductsViewModelFactory
    internal lateinit var viewModel: AllProductsViewModel
    private val args: AllProductsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_all_products,
                container,
                false
            )

        viewModelFactory = AllProductsViewModelFactory(
            sliderType = args.sliderType,
            storeId = if (args.storeId == -1) null else args.storeId
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(AllProductsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = when (args.sliderType) {
            ApiConstant.SliderOfferType.INNER_LATEST_PRODUCTS -> getString(R.string.latest_products)
            ApiConstant.SliderOfferType.INNER_BEST_SELLING -> getString(R.string.best_selling)
            ApiConstant.SliderOfferType.INNER_LATEST_OFFERS -> getString(R.string.latest_offers)
            else -> null
        }

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup pagination
        viewModel.productsPagedList =
            PaginationDataSource.initializedPagedListBuilder<ProductModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllProductsFragment,
            ).build()

        // setup observers
        if (args.sliderType != ApiConstant.SliderOfferType.SEARCH_RESULTS) {
            val sliderOffersAdapter0 = OffersSliderAdapter(this)
            binding.sliderOffersLceeLayoutVp.adapter = sliderOffersAdapter0
            TabLayoutMediator(
                binding.sliderOffersLceeLayoutTl,
                binding.sliderOffersLceeLayoutVp
            ) { tab, position ->
                //Some implementation
            }.attach()
            viewModel.sliderOffersResponse.observe(viewLifecycleOwner, {
                viewModel.sliderOffersLcee.value!!.response.value = it
                sliderOffersAdapter0.submitList(it.data)
            })
            viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
                binding.sliderOffersLceeLayoutVp.currentItem = it
            })
        }

        val productsAdapter = PagedProductsAdapter(this, editWishList = this::editWishList)
        binding.productsRv.adapter = productsAdapter
        viewModel.productsPagedList?.observe(viewLifecycleOwner, {
            productsAdapter.submitList(it)
        })

        // setup click listener


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