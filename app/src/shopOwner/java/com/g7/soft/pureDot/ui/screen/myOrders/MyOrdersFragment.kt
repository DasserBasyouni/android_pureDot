package com.g7.soft.pureDot.ui.screen.myOrders

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OrdersAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentMyOrdersBinding
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch

class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    internal lateinit var viewModelFactory: MyOrdersViewModelFactory
    internal lateinit var viewModel: MyOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_orders, container, false)

        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())

            viewModelFactory = MyOrdersViewModelFactory(
                tokenId = tokenId
            )
            viewModel = ViewModelProvider(
                this@MyOrdersFragment,
                viewModelFactory
            ).get(MyOrdersViewModel::class.java)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this@MyOrdersFragment
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.ordersPagedList =
            PaginationDataSource.initializedPagedListBuilder<MasterOrderModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@MyOrdersFragment,
            ).build()

        // setup observers
        val myOrdersAdapter = OrdersAdapter(this)
        binding.ordersRv.adapter = myOrdersAdapter
        viewModel.ordersPagedList?.observe(viewLifecycleOwner, {
            myOrdersAdapter.submitList(it)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_filter, menu)
    }
}