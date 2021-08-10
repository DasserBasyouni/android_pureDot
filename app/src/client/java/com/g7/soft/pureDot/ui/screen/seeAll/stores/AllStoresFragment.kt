package com.g7.soft.pureDot.ui.screen.seeAll.stores

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedStoresAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllStoresBinding
import com.g7.soft.pureDot.model.StoreModel

class AllStoresFragment : Fragment() {
    private lateinit var binding: FragmentAllStoresBinding
    internal lateinit var viewModel: AllStoresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_all_stores, container, false)

        viewModel = ViewModelProvider(this).get(AllStoresViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.storesPagedList =
            PaginationDataSource.initializedPagedListBuilder<StoreModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllStoresFragment,
            ).build()

        // setup observers
        val categoriesAdapter = PagedStoresAdapter(this)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.storesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}