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
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentCheckout3Binding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.kofigyan.stateprogressbar.StateProgressBar
import com.mastercard.gateway.android.sdk.Gateway
import com.mastercard.gateway.android.sdk.GatewayCallback
import com.mastercard.gateway.android.sdk.GatewayMap
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch
import timber.log.Timber


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
        viewModel.masterOrderResponse.observe(viewLifecycleOwner, {
            viewModel.orderLcee.value!!.response.value = it
        })
        viewModel.masterOrderResponse.observe(viewLifecycleOwner, {
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
        /*binding.couponTil.editText?.setOnFocusChangeListener { _, hasFocus ->
            Log.e("Z_", "hasFocus: $hasFocus")
            if (hasFocus) {
                Toast.makeText(requireContext(), "Got the focus", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Lost the focus", Toast.LENGTH_LONG).show()
            }
        }*/
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

                    viewModel.masterOrderResponse.value?.data?.id = it?.masterOrderId
                    viewModel.masterOrderResponse.value?.data?.number = it?.orderNumber
                    viewModel.masterOrderResponse.value?.data?.totalOrderCost = it?.orderAmount

                    if (viewModel.isCashOnDelivery.value == true) {
                        CartRepository("").clearCart(lifecycleScope, requireContext())

                        ProjectDialogUtils.showSimpleMessage(
                            context = requireContext(),
                            messageResId = R.string.order_placed_successfully,
                            drawableResId = R.drawable.ic_successfully,
                            positiveBtnTextResId = R.string.track_order
                        ) {
                            val bundle =
                                bundleOf("masterOrderId" to viewModel.masterOrderResponse.value?.data?.id)
                            findNavController().navigate(
                                R.id.action_checkoutFragment_to_orderFragment, bundle
                            )
                        }
                    } else {
                        viewModel.createMasterCardSession(
                            requireActivity().currentLocale.toLanguageTag(),
                            it?.description
                        ).observeApiResponse(this@CheckoutConfirmationFragment, { sessionModel ->
                            processedPayment(tokenId, sessionModel?.id)
                        })
                    }
                })
            }
        }
    }


    private fun processedPayment(tokenId: String?, sessionId: String?) {
        val gateway = Gateway()
        gateway.merchantId = ApiConstant.MERCHANT_ID
        gateway.region = Gateway.Region.ASIA_PACIFIC

        val request = GatewayMap()
            .set(
                "sourceOfFunds.provided.card.nameOnCard",
                viewModel.masterCardNameOnCard.value
            )
            .set("sourceOfFunds.provided.card.number", viewModel.masterCardNumber.value)
            .set(
                "sourceOfFunds.provided.card.securityCode",
                viewModel.masterCardSecurityCode.value
            )
            .set(
                "sourceOfFunds.provided.card.expiry.month",
                viewModel.masterCardExpiryMonth.value
            )
            .set(
                "sourceOfFunds.provided.card.expiry.year",
                viewModel.masterCardExpiryYear.value
            )

        gateway.updateSession(
            sessionId,
            ApiConstant.MASTER_CARD_API_VERSION,
            request,
            object : GatewayCallback {
                override fun onSuccess(response: GatewayMap?) {
                    submitIsPaid(true)
                }

                override fun onError(throwable: Throwable?) {
                    submitIsPaid(false)
                    Timber.e(throwable)
                }

                private fun submitIsPaid(isPaid: Boolean) {
                    viewModel.checkoutIsPaid(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        isPaid = isPaid
                    ).observeApiResponse(this@CheckoutConfirmationFragment, {
                        if (isPaid) {
                            CartRepository("").clearCart(lifecycleScope, requireContext())

                            ProjectDialogUtils.showSimpleMessage(
                                context = requireContext(),
                                messageResId = R.string.order_placed_successfully,
                                drawableResId = R.drawable.ic_successfully,
                                positiveBtnTextResId = R.string.track_order
                            ) {
                                val bundle =
                                    bundleOf("masterOrderId" to viewModel.masterOrderResponse.value?.data?.id)
                                findNavController().navigate(
                                    R.id.action_checkoutFragment_to_orderFragment,
                                    bundle
                                )
                            }
                        } else {
                            ProjectDialogUtils.showSimpleMessage(
                                context = requireContext(),
                                messageResId = R.string.error_payment_failed,
                                drawableResId = R.drawable.ic_secure_shield
                            )
                        }
                    })
                }
            })
    }
}