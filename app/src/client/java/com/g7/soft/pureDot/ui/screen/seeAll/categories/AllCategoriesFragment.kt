package com.g7.soft.pureDot.ui.screen.seeAll.categories

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllCategoriesBinding
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration

class AllCategoriesFragment : Fragment() {
    private lateinit var binding: FragmentAllCategoriesBinding
    internal lateinit var viewModel: AllCategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_all_categories, container, false)

        viewModel = ViewModelProvider(this).get(AllCategoriesViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.categoriesPagedList =
            PaginationDataSource.initializedPagedListBuilder<CategoryModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllCategoriesFragment,
            ).build()

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 50, true))

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}