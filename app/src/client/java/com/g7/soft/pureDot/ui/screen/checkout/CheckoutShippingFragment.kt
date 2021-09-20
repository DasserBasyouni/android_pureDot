package com.g7.soft.pureDot.ui.screen.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductCartReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckout2Binding
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.kofigyan.stateprogressbar.StateProgressBar
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class CheckoutShippingFragment(
    private val viewModel: CheckoutViewModel,
    private val currencySymbol: String
) : Fragment() {

    private lateinit var binding: FragmentCheckout2Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout_2, container, false)

        binding.currency = currencySymbol
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getShippingMethods(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.shippingMethodsResponse.observe(viewLifecycleOwner, {
            viewModel.shippingMethodsLcee.value!!.response.value = it
        })
        viewModel.selectedShippingMethodId.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.checkCartItems(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                )
            }
        })
        viewModel.orderResponse.observe(viewLifecycleOwner, {
            viewModel.orderLcee.value!!.response.value = it
        })

        // setup adapter
        ProductCartReviewHeaderAdapter(this, viewModel.masterOrder?.orders).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }

        // setup listeners
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.haveSelectedShippingMethod.value =
                binding.radioGroup.findViewById<View>(checkedId) != null

            viewModel.selectedShippingMethodId.value =
                binding.radioGroup.findViewById<View>(checkedId).tag?.toString()
        }
        binding.nextBtn.setOnClickListener {
            if (viewModel.haveSelectedShippingMethod.value == true)
                viewModel.currentStateNumber.value = StateProgressBar.StateNumber.THREE
            else
                ProjectDialogUtils.showSimpleMessage(
                    requireContext(),
                    R.string.shipping_method_is_required,
                    R.drawable.ic_secure_shield
                )
        }
    }

}