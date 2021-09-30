package com.g7.soft.pureDot.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OrdersAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        var refreshData: ((String?) -> Unit)? = null
        var isRunning = false
    }


    private lateinit var binding: FragmentHomeBinding
    internal lateinit var viewModelFactory: HomeViewModelFactory
    internal lateinit var viewModel: HomeViewModel


    override fun onStart() {
        super.onStart()
        OrderFragment.isRunning = true
    }

    override fun onStop() {
        super.onStop()
        OrderFragment.isRunning = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())

            viewModelFactory = HomeViewModelFactory(
                tokenId = tokenId
            )
            viewModel = ViewModelProvider(
                this@HomeFragment,
                viewModelFactory
            ).get(HomeViewModel::class.java)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this@HomeFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshData = {
            viewModel.newOrdersPagedList?.value?.dataSource?.invalidate()
            viewModel.pendingOrdersPagedList?.value?.dataSource?.invalidate()
        }

        // setup observers
        viewModel.areNewOrdersSelected.observe(viewLifecycleOwner, { isNewOrder ->
            val myOrdersAdapter = OrdersAdapter(this, true)
            binding.ordersRv.adapter = myOrdersAdapter

            if (isNewOrder == false) {
                // setup pagination
                viewModel.pendingOrdersPagedList =
                    PaginationDataSource.initializedPagedListBuilder<MasterOrderModel>(
                        config = PagedList.Config.Builder()
                            .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                            .setEnablePlaceholders(true)
                            .build(),
                        fragment = this@HomeFragment,
                    ).build()

                // setup observers
                viewModel.pendingOrdersPagedList?.observe(viewLifecycleOwner, {
                    myOrdersAdapter.submitList(it)
                })

            } else {
                // setup pagination
                viewModel.newOrdersPagedList =
                    PaginationDataSource.initializedPagedListBuilder<MasterOrderModel>(
                        config = PagedList.Config.Builder()
                            .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                            .setEnablePlaceholders(true)
                            .build(),
                        fragment = this@HomeFragment,
                    ).build()

                // setup observers
                viewModel.newOrdersPagedList?.observe(viewLifecycleOwner, {
                    myOrdersAdapter.submitList(it)
                })
            }
        })


        // setup onClick
        binding.newOrderLayout.root.setOnClickListener {
            viewModel.areNewOrdersSelected.value = true
        }
        binding.pendingOrdersLayout.root.setOnClickListener {
            viewModel.areNewOrdersSelected.value = false
        }
    }
}