package com.g7.soft.pureDot.ui.screen.addMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentAddMoneyBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.g7.soft.pureDot.utils.ValidationUtils
import com.mastercard.gateway.android.sdk.Gateway
import com.mastercard.gateway.android.sdk.GatewayCallback
import com.mastercard.gateway.android.sdk.GatewayMap
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch
import timber.log.Timber

class AddMoneyFragment : Fragment() {
    private lateinit var binding: FragmentAddMoneyBinding
    internal lateinit var viewModel: AddMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_add_money,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(AddMoneyViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup observables
        viewModel.stcPayAuthResponse.observeApiResponse(this, {
            ProjectDialogUtils.showStcPaymentConfirmation(
                requireContext(),
                positiveCallback = { stcMobileOtp ->
                    viewModel.stcMobileOtp.value = stcMobileOtp

                    viewModel.confirmStcPay(requireActivity().currentLocale.toLanguageTag())
                        .observeApiResponse(this@AddMoneyFragment, {
                            lifecycleScope.launch {
                                val tokenId = UserRepository("").getTokenId(requireContext())
                                submitIsPaid(true, tokenId)
                            }
                        })
                })
        })
        viewModel.addMoneyResponse.observeApiResponse(this@AddMoneyFragment, {
            when {
                viewModel.isStcPayChecked.value == true -> {
                    viewModel.authenticateStcPay(
                        requireActivity().currentLocale.toLanguageTag()
                    )
                }
                viewModel.isMasterCardChecked.value == true -> {
                    viewModel.createMasterCardSession(
                        requireActivity().currentLocale.toLanguageTag()
                    ).observeApiResponse(
                        this@AddMoneyFragment,
                        { sessionModel ->
                            lifecycleScope.launch {
                                val tokenId = UserRepository("").getTokenId(requireContext())
                                processedMasterCardPayment(tokenId, sessionModel?.id)
                            }
                        })
                }
            }
        }, validationObserve = {
            binding.amountTil.error =
                if (it == ProjectConstant.Companion.ValidationError.EMPTY_AMOUNT)
                    getString(R.string.error_empty_amount) else null
        })

        // setup listeners
        binding.stcPayIv.setOnClickListener {
            binding.stcPayIv.isChecked = true
            viewModel.isStcPayChecked.value = true

            // reset other methods
            binding.masterCardIv.isChecked = false
            viewModel.isMasterCardChecked.value = false
        }
        binding.masterCardIv.setOnClickListener {
            binding.masterCardIv.isChecked = true
            viewModel.isMasterCardChecked.value = true

            // reset other methods
            binding.stcPayIv.isChecked = false
            viewModel.isStcPayChecked.value = false
        }
        binding.addMoneyBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                // validate inputs
                when {
                    viewModel.isStcPayChecked.value == true -> {
                        ValidationUtils()
                            .setPhoneNumber(viewModel.stcPhoneNumber.value)
                            .getError()?.let {
                                binding.stcPhoneNumberTil.error =
                                    if (it == ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER)
                                        getString(R.string.error_empty_phone_number) else null

                                return@launch
                            }
                    }
                    viewModel.isMasterCardChecked.value == true -> {
                        ValidationUtils()
                            .setNameOnCard(viewModel.masterCardNameOnCard.value)
                            .setCardNumber(viewModel.masterCardNumber.value)
                            .setCardSecurityCode(viewModel.masterCardSecurityCode.value)
                            .setCardExpiryMonth(viewModel.masterCardExpiryMonth.value)
                            .setCardExpiryYear(viewModel.masterCardExpiryYear.value)
                            .getError()?.let {
                                binding.nameOnCardTil.error =
                                    if (it == ProjectConstant.Companion.ValidationError.EMPTY_NAME_ON_CARD)
                                        getString(R.string.error_empty_name_on_card) else null

                                binding.cardNumberTil.error =
                                    if (it == ProjectConstant.Companion.ValidationError.EMPTY_CARD_NUMBER)
                                        getString(R.string.error_empty_card_number) else null

                                binding.cardSecurityCodeTil.error =
                                    if (it == ProjectConstant.Companion.ValidationError.EMPTY_CARD_SECURITY_CODE)
                                        getString(R.string.error_empty_security_code) else null

                                binding.cardExpiryMonthTil.error =
                                    when (it) {
                                        ProjectConstant.Companion.ValidationError.EMPTY_CARD_EXPIRY_MONTH -> getString(
                                            R.string.error_empty_expiry_month
                                        )
                                        ProjectConstant.Companion.ValidationError.INVALID_CARD_EXPIRY_MONTH -> getString(
                                            R.string.error_invalid_expiry_month
                                        )
                                        else -> null
                                    }

                                binding.cardExpiryYearTil.error =
                                    if (it == ProjectConstant.Companion.ValidationError.EMPTY_CARD_EXPIRY_YEAR)
                                        getString(R.string.error_empty_expiry_year) else null

                                return@launch
                            }
                    }
                }

                viewModel.addMoney(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                )
            }
        }
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
        viewModel.addMoneyIsPaid(
            requireActivity().currentLocale.toLanguageTag(),
            tokenId = tokenId,
            isPaid = isPaid
        ).observeApiResponse(this@AddMoneyFragment, {
            if (isPaid) {
                findNavController().navigateUp()
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
        findNavController().popBackStack()
    }
}