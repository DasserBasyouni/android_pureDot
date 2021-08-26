package com.g7.soft.pureDot.ui.screen.productCheckout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductCartReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckout3Binding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.kofigyan.stateprogressbar.StateProgressBar
import com.zeugmasolutions.localehelper.currentLocale


class ProductCheckoutConfirmationFragment(private val viewModel: ProductCheckoutViewModel) : Fragment() {

    private lateinit var binding: FragmentCheckout3Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout_3, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup adapter
        ProductCartReviewHeaderAdapter(this, viewModel.storesProductsCartDetails).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }

        // setup listeners
        binding.paymentMethodCv.setOnClickListener {
            viewModel.currentStateNumber.value = StateProgressBar.StateNumber.THREE
        }

        binding.checkoutBtn.setOnClickListener {
            // move this to onActivityResults?
            val tokenId = "" //todo
            viewModel.checkoutIsPaid(
                requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                paidAmount = 0.0
            ).observeApiResponse(this, {
                ProjectDialogUtils.showSimpleMessage(
                    context = requireContext(),
                    messageResId = R.string.order_placed_successfully,
                    drawableResId = R.drawable.ic_successfully,
                    positiveBtnTextResId = R.string.track_order
                ) {
                    val bundle = bundleOf("masterOrder" to it)
                    findNavController().navigate(
                        R.id.action_checkoutFragment_to_orderFragment,
                        bundle
                    )
                }
            })
        }
    }
}