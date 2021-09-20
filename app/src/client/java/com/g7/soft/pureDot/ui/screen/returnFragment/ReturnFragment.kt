package com.g7.soft.pureDot.ui.screen.returnFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OrderReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentReturnBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


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

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModelFactory = ReturnViewModelFactory(
                masterOrder = args.masterOrder,
                selectedProduct = args.selectedProduct,
            )
            viewModel = ViewModelProvider(
                this@ReturnFragment,
                viewModelFactory
            ).get(RefundViewModel::class.java)

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.selectedProduct = args.selectedProduct
            binding.lifecycleOwner = this@ReturnFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getShippingMethods(requireActivity().currentLocale.toLanguageTag())

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
        viewModel.shippingMethodsResponse.observe(viewLifecycleOwner, {
            viewModel.shippingMethodsLcee.value!!.response.value = it
        })
        viewModel.shippingCostResponse.observe(viewLifecycleOwner, {
            viewModel.shippingCostLcee.value!!.response.value = it
        })
        viewModel.selectedShippingMethodId.observe(viewLifecycleOwner, {
            viewModel.calculateRefundShipping(
                requireActivity().currentLocale.toLanguageTag()
            )
        })

        // setup listeners
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.haveSelectedShippingMethod.value =
                binding.radioGroup.findViewById<View>(checkedId) != null

            viewModel.selectedShippingMethodId.value =
                binding.radioGroup.findViewById<View>(checkedId).tag?.toString()
        }
        binding.confirmationBtn.setOnClickListener {
            viewModel.returnItem(requireActivity().currentLocale.toLanguageTag())
                .observeApiResponse(this, {
                    findNavController().navigate(R.id.action_refundFragment_to_myOrdersFragment)
                })
        }
    }
}