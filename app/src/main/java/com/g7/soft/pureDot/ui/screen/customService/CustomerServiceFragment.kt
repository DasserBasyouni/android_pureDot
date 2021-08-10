package com.g7.soft.pureDot.ui.screen.customService

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ComplainsAdapter
import com.g7.soft.pureDot.databinding.FragmentCustomerServiceBinding
import com.zeugmasolutions.localehelper.currentLocale

class CustomerServiceFragment : Fragment() {
    private lateinit var binding: FragmentCustomerServiceBinding
    internal lateinit var viewModel: CustomerServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_customer_service, container, false)

        viewModel = ViewModelProvider(this).get(CustomerServiceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" //todo
        viewModel.getComplains(requireActivity().currentLocale.toLanguageTag(), tokenId)

        // setup observers
        val myOrdersAdapter = ComplainsAdapter(this)
        binding.complainsRv.adapter = myOrdersAdapter
        viewModel.complainResponse.observe(viewLifecycleOwner, {
            viewModel.complainLcee.value!!.response.value = it
            myOrdersAdapter.submitList(it.data)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}