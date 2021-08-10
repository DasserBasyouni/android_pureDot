package com.g7.soft.pureDot.ui.screen.store

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OffersSliderAdapter
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.StaggeredProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentStoreBinding
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*

class StoreFragment : Fragment() {
    private lateinit var binding: FragmentStoreBinding
    private lateinit var viewModelFactory: StoreViewModelFactory
    internal lateinit var viewModel: StoreViewModel
    private val args: StoreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_store, container, false)

        viewModelFactory = StoreViewModelFactory(
            store = args.store,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(StoreViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.store.name

        // setup pagination
        viewModel.categoriesPagedList =
            PaginationDataSource.initializedPagedListBuilder<CategoryModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@StoreFragment,
            ).build()

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 30, true))

        val sliderOffersAdapter = OffersSliderAdapter(this)
        binding.sliderOffersLceeLayoutVp.adapter = sliderOffersAdapter
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl,
            binding.sliderOffersLceeLayoutVp
        ) { _, _ -> }.attach()
        viewModel.sliderOffersResponse.observe(viewLifecycleOwner, {
            viewModel.sliderOffersLcee.value!!.response.value = it
            sliderOffersAdapter.submitList(it.data)
        })
        viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp.currentItem = it
        })

        val latestOffersAdapter = ProductsAdapter(this)
        binding.latestOffersRv.adapter = latestOffersAdapter
        viewModel.latestOffersResponse.observe(viewLifecycleOwner, {
            viewModel.latestOffersLcee.value!!.response.value = it
            latestOffersAdapter.submitList(it.data?.data)
        })

        val latestProductsAdapter = StaggeredProductsAdapter(this)
        binding.latestProductsRv.adapter = latestProductsAdapter
        viewModel.latestProductsResponse.observe(viewLifecycleOwner, {
            viewModel.latestProductsLcee.value!!.response.value = it
            latestProductsAdapter.submitList(it.data?.data)
        })

        // setup click listener
        binding.latestOffersSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_OFFERS,
                "storeId" to args.store.id
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
        binding.latestProductsSellAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_PRODUCTS,
                "storeId" to args.store.id
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
    }

}