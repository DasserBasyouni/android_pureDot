package com.g7.soft.pureDot.ui.screen.myOrders

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OrdersAdapter
import com.g7.soft.pureDot.databinding.FragmentMyOrdersBinding
import com.zeugmasolutions.localehelper.currentLocale

class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    internal lateinit var viewModel: MyOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_orders, container, false)

        viewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" //todo
        viewModel.getMyOrders(requireActivity().currentLocale.toLanguageTag(), tokenId)

        // setup observers
        val myOrdersAdapter = OrdersAdapter(this)
        binding.ordersRv.adapter = myOrdersAdapter
        viewModel.ordersResponse.observe(viewLifecycleOwner, {
            Log.e("Z_", "it: ${it.exception}")
            viewModel.ordersLcee.value!!.response.value = it
            myOrdersAdapter.submitList(it.data)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}