package com.g7.soft.pureDot.ui.screen.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.databinding.FragmentCheckoutPaymentBinding
import com.g7.soft.pureDot.utils.ValidationUtils
import com.kofigyan.stateprogressbar.StateProgressBar


class CheckoutPaymentFragment(private val viewModel: CheckoutViewModel) : Fragment() {

    private lateinit var binding: FragmentCheckoutPaymentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_checkout_payment,
                container,
                false
            )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe payment method
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.isCashOnDelivery.value = checkedId == R.id.cashOnDelivery
        }

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
        binding.nextBtn.setOnClickListener {

            // validate inputs
            if (viewModel.isCashOnDelivery.value != true)
                if (viewModel.isMasterCardChecked.value == true) {
                    ValidationUtils()
                        .setNameOnCard(viewModel.masterCardNameOnCard.value)
                        .setCardNumber(viewModel.masterCardNumber.value)
                        .setCardSecurityCode(viewModel.masterCardSecurityCode.value)
                        .setCardExpiryMonth(viewModel.masterCardExpiryMonth.value)
                        .setCardExpiryYear(viewModel.masterCardExpiryYear.value)
                        .getError()?.let {
                            binding.nameOnCardTil.error =
                                if (it == ValidationError.EMPTY_NAME_ON_CARD)
                                    getString(R.string.error_empty_name_on_card) else null

                            binding.cardNumberTil.error =
                                if (it == ValidationError.EMPTY_CARD_NUMBER)
                                    getString(R.string.error_empty_card_number) else null

                            binding.cardSecurityCodeTil.error =
                                if (it == ValidationError.EMPTY_CARD_SECURITY_CODE)
                                    getString(R.string.error_empty_security_code) else null

                            binding.cardExpiryMonthTil.error =
                                when (it) {
                                    ValidationError.EMPTY_CARD_EXPIRY_MONTH -> getString(R.string.error_empty_expiry_month)
                                    ValidationError.INVALID_CARD_EXPIRY_MONTH -> getString(R.string.error_invalid_expiry_month)
                                    else -> null
                                }

                            binding.cardExpiryYearTil.error =
                                if (it == ValidationError.EMPTY_CARD_EXPIRY_YEAR)
                                    getString(R.string.error_empty_expiry_year) else null

                            return@setOnClickListener
                        }

                } else if (viewModel.isStcPayChecked.value == true) {
                    ValidationUtils()
                        .setPhoneNumber(viewModel.stcPhoneNumber.value)
                        .getError()?.let {
                            binding.stcPhoneNumberTil.error =
                                if (it == ValidationError.EMPTY_PHONE_NUMBER)
                                    getString(R.string.error_empty_phone_number) else null

                            return@setOnClickListener
                        }
                }

            viewModel.currentStateNumber.value =
                if (viewModel.masterOrder?.isService == true) StateProgressBar.StateNumber.THREE
                else StateProgressBar.StateNumber.FOUR
        }

        // init selection
        binding.stcPayIv.isChecked = true
    }

}