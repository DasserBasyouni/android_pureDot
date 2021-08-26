package com.g7.soft.pureDot.ui.screen.returnFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OrderReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentReturnBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.zeugmasolutions.localehelper.currentLocale


class ReturnFragment : Fragment() {

    internal lateinit var binding: FragmentReturnBinding
    private lateinit var viewModelFactory: ReturnViewModelFactory
    internal lateinit var viewModel: RefundViewModel
    private val args: ReturnFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_return, container, false)

        viewModelFactory = ReturnViewModelFactory(
            masterOrder = args.masterOrder,
            selectedProduct = args.selectedProduct,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(RefundViewModel::class.java)

        binding.viewModel = viewModel
        binding.selectedProduct = args.selectedProduct
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init data
        OrderReviewHeaderAdapter(
            args.masterOrder,
            this,
            areTotalPricesVisible = false,
            selectedProduct = viewModel.selectedProduct
        ).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }

        // setup observables
        viewModel.shippingCostResponse.observe(viewLifecycleOwner, {
            viewModel.shippingCostLcee.value!!.response.value = it
        })
        viewModel.selectedShippingMethodViewId.observe(viewLifecycleOwner, {
            viewModel.calculateRefundShipping(
                requireActivity().currentLocale.toLanguageTag()
            )
        })

        // setup listeners
        binding.confirmationBtn.setOnClickListener {
            viewModel.refund(requireActivity().currentLocale.toLanguageTag()).observeApiResponse(this, {
                findNavController().navigate(R.id.action_refundFragment_to_myOrdersFragment)
            })
        }
    }
}