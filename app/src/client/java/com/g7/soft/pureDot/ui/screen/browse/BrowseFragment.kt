package com.g7.soft.pureDot.ui.screen.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.adapter.PagedStoresAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentBrowseBinding
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration

class BrowseFragment : Fragment() {
    private lateinit var binding: FragmentBrowseBinding
    internal lateinit var viewModel: BrowseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_browse, container, false)


        viewModel = ViewModelProvider(this).get(BrowseViewModel::class.java)

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
                fragment = this@BrowseFragment,
            ).build()
        viewModel.storesPagedList =
            PaginationDataSource.initializedPagedListBuilder<StoreModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@BrowseFragment,
                isFirstPagedList = false
            ).build()

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })
        val storesAdapter = PagedStoresAdapter(this)
        binding.storesRv.adapter = storesAdapter
        viewModel.storesPagedList?.observe(viewLifecycleOwner, {
            storesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 30, true))
    }

}