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
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentCheckout3Binding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.g7.soft.pureDot.utils.ValidationUtils
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

        setupAdapter()

        // setup observables
        viewModel.stcPayAuthResponse.observeApiResponse(this, {
            ProjectDialogUtils.showStcPaymentConfirmation(
                requireContext(),
                positiveCallback = { stcMobileOtp ->
                    viewModel.stcMobileOtp.value = stcMobileOtp

                    viewModel.confirmStcPay(requireActivity().currentLocale.toLanguageTag())
                        .observeApiResponse(this@CheckoutConfirmationFragment, {
                            lifecycleScope.launch {
                                val tokenId = UserRepository("").getTokenId(requireContext())
                                submitIsPaid(true, tokenId)
                            }
                        })
                })
        })
        viewModel.masterOrderResponse.observe(viewLifecycleOwner, {
            viewModel.orderLcee.value!!.response.value = it
            setupAdapter()

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
        binding.addRemoveCouponBtn.setOnClickListener {
            val isCouponApplied = viewModel.isCouponApplied.value == true

            // validate inputs
            ValidationUtils()
                .setCouponCode(viewModel.couponCode.value)
                .getError()?.let {
                    binding.couponTil.error =
                        if (!isCouponApplied && it == ProjectConstant.Companion.ValidationError.EMPTY_COUPON_CODE)
                            getString(R.string.error_empty_coupon_code) else null

                    return@setOnClickListener
                }

            if (isCouponApplied) {
                viewModel.couponCode.value = null
                viewModel.isCouponApplied.value = false
            } else
                viewModel.isCouponApplied.value = true

            binding.couponTil.error = null
            binding.couponTil.isErrorEnabled = false

            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.checkCartItems(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                )
            }

            changeCouponButton(isCouponApplied)
        }
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
                ).observeApiResponse(
                    this@CheckoutConfirmationFragment,
                    {
                        viewModel.masterOrderResponse.value?.data?.id =
                            it?.checkoutSuccessResponse?.masterOrderId
                        viewModel.masterOrderResponse.value?.data?.number =
                            it?.checkoutSuccessResponse?.orderNumber
                        viewModel.masterOrderResponse.value?.data?.totalOrderCost =
                            it?.checkoutSuccessResponse?.orderAmount

                        when {
                            viewModel.isCashOnDelivery.value == true || viewModel.isDigitalWallet.value == true -> {
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

                            }
                            viewModel.isStcPayChecked.value == true -> {
                                viewModel.authenticateStcPay(
                                    requireActivity().currentLocale.toLanguageTag(),
                                    it?.checkoutSuccessResponse?.description
                                )
                            }
                            viewModel.isMasterCardChecked.value == true -> {
                                viewModel.createMasterCardSession(
                                    requireActivity().currentLocale.toLanguageTag(),
                                    it?.checkoutSuccessResponse?.description
                                ).observeApiResponse(
                                    this@CheckoutConfirmationFragment,
                                    { sessionModel ->
                                        processedMasterCardPayment(tokenId, sessionModel?.id)
                                    })
                            }
                        }
                    },
                    apiStatusToObserve = arrayOf(
                        ApiConstant.Status.ORDER_LIMIT,
                        ApiConstant.Status.INVALID_CART_ITEMS
                    ),
                    chosenApiStatusObserve = { status, data ->
                        if (status == ApiConstant.Status.ORDER_LIMIT)
                            ProjectDialogUtils.showSimpleMessage(
                                requireContext(),
                                message = getString(
                                    R.string.msg_order_does_not_meet_min_,
                                    data?.orderMinimumCharge
                                ),
                                drawableResId = R.drawable.ic_secure_shield
                            )
                        else if (status == ApiConstant.Status.INVALID_CART_ITEMS) {
                            val firstUnavailableProduct =
                                data?.orderModel?.firstOrder?.products?.firstOrNull { it.isAvailable == false }

                            if (firstUnavailableProduct != null) {
                                ProjectDialogUtils.showSimpleMessage(
                                    requireContext(),
                                    message = getString(
                                        R.string.error_unavailable_product_,
                                        firstUnavailableProduct.name
                                    ),
                                    drawableResId = R.drawable.ic_secure_shield
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun setupAdapter() {
        ProductCartReviewHeaderAdapter(
            this, viewModel.masterOrderResponse.value?.data?.orders
        ).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }
    }

    private fun changeCouponButton(isCouponApplied: Boolean) {
        binding.addRemoveCouponBtn.backgroundTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (isCouponApplied) {
                R.color.bluish
            } else {
                R.color.driverRed
            }
        )
    }

    private fun processedMasterCardPayment(tokenId: String?, sessionId: String?) {
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
                    submitIsPaid(true, tokenId)
                }

                override fun onError(throwable: Throwable?) {
                    submitIsPaid(false, tokenId)
                    Timber.e(throwable)
                }
            })
    }

    private fun submitIsPaid(isPaid: Boolean, tokenId: String?) {
        viewModel.checkoutIsPaid(
            requireActivity().currentLocale.toLanguageTag(),
            tokenId = tokenId,
            isPaid = isPaid
        ).observeApiResponse(this@CheckoutConfirmationFragment, {
            if (isPaid) {
                processedPaymentSuccess()
            } else {
                ProjectDialogUtils.showSimpleMessage(
                    context = requireContext(),
                    messageResId = R.string.error_payment_failed,
                    drawableResId = R.drawable.ic_secure_shield
                )
            }
        })
    }

    private fun processedPaymentSuccess() {
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
    }
}