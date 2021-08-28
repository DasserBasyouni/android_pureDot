package com.g7.soft.pureDot.ui.screen.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.adapter.PagedStoresAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentFilterBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration

class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    internal val viewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private val args: FilterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_filter, container, false)

        viewModel.currency = args.currency

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        //viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup pagination
        viewModel.categoriesPagedList =
            PaginationDataSource.initializedPagedListBuilder<CategoryModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@FilterFragment,
            ).build()
        viewModel.storesPagedList =
            PaginationDataSource.initializedPagedListBuilder<StoreModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@FilterFragment,
                isFirstPagedList = false
            ).build()

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true, isSelectable = true)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })
        val storesAdapter = PagedStoresAdapter(this, isSelectable = true)
        binding.storesRv.adapter = storesAdapter
        viewModel.storesPagedList?.observe(viewLifecycleOwner, {
            storesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 30, true))

        // setup click listener
        binding.applyBtn.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.SEARCH_RESULTS,
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
        binding.resetFilterTv.makeLinks(Pair(getString(R.string.reset_filter), View.OnClickListener {
            viewModel.resetFilter()
        }), doChangeColor = false)
    }

}