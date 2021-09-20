package com.g7.soft.pureDot.ui.screen.checkout

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductCartReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckout3Binding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.kofigyan.stateprogressbar.StateProgressBar
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class CheckoutConfirmationFragment(
    private val viewModel: CheckoutViewModel,
    private val currencySymbol: String
) :
    Fragment() {

    private lateinit var binding: FragmentCheckout3Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout_3, container, false)

        binding.currency = currencySymbol
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@CheckoutConfirmationFragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add line through coupon discount
        binding.couponDiscountTv.paintFlags =
            binding.couponDiscountTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        // setup adapter
        ProductCartReviewHeaderAdapter(this, viewModel.masterOrder?.orders).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }

        // setup observables
        viewModel.orderResponse.observe(viewLifecycleOwner, {
            viewModel.orderLcee.value!!.response.value = it
        })
        viewModel.orderResponse.observe(viewLifecycleOwner, {
            binding.couponTil.editText?.setCompoundDrawablesRelative(
                null,
                null,
                if (it.data?.totalCouponDiscount != null && it.data.totalCouponDiscount != 0.0) ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_check
                ) else null, null
            )
        })

        // setup listeners
        binding.paymentMethodCv.setOnClickListener {
            viewModel.currentStateNumber.value = StateProgressBar.StateNumber.THREE
        }
        binding.couponTil.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lifecycleScope.launch {
                    val tokenId = UserRepository("").getTokenId(requireContext())

                    viewModel.checkCartItems(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId
                    )
                }
                true
            } else false
        }
        binding.checkoutBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.checkout(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                ).observeApiResponse(this@CheckoutConfirmationFragment, {

                    CartRepository("").clearCart(lifecycleScope, requireContext())

                    viewModel.orderResponse.value?.data?.id = it?.id

                    if (viewModel.selectedShippingMethodId.value == null) // in case on delivery (delivery app)
                        ProjectDialogUtils.showSimpleMessage(
                            context = requireContext(),
                            messageResId = R.string.order_placed_successfully,
                            drawableResId = R.drawable.ic_successfully,
                            positiveBtnTextResId = R.string.track_order
                        ) {
                            val bundle =
                                bundleOf("masterOrderId" to viewModel.orderResponse.value?.data?.id)
                            findNavController().navigate(
                                R.id.action_checkoutFragment_to_orderFragment, bundle
                            )
                        }
                    else // todo launch payment gateway & move to onAcitivtyResutls
                        viewModel.checkoutIsPaid(
                            requireActivity().currentLocale.toLanguageTag(),
                            tokenId = tokenId,
                            isPaid = true // todo depends on gatway return
                        ).observeApiResponse(this@CheckoutConfirmationFragment, {
                            ProjectDialogUtils.showSimpleMessage(
                                context = requireContext(),
                                messageResId = R.string.order_placed_successfully,
                                drawableResId = R.drawable.ic_successfully,
                                positiveBtnTextResId = R.string.track_order
                            ) {
                                val bundle =
                                    bundleOf("masterOrderId" to viewModel.orderResponse.value?.data?.id)
                                findNavController().navigate(
                                    R.id.action_checkoutFragment_to_orderFragment, bundle
                                )
                            }
                        })
                })
            }
        }
    }
}