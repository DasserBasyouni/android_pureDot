package com.g7.soft.pureDot.ui.screen.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentForgetPasswordBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.zeugmasolutions.localehelper.currentLocale

class ForgetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgetPasswordBinding
    private lateinit var viewModel: ForgetPasswordViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_forget_password,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup listeners
        binding.submitBtn.setOnClickListener {
            viewModel.forgetPassword(requireActivity().currentLocale.toLanguageTag())
                .observeApiResponse(this, {
                    findNavController().navigate(
                        ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToPhoneVerificationFragment(
                            isPasswordReset = true,
                            isWalletVerification = false,
                            emailOrPhoneNumber = viewModel.emailOrPhoneNumber.value
                        )
                    )
                }, validationObserve = {
                    binding.emailOrPhoneNumberTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER_OR_EMAIL)
                            getString(R.string.error_empty_phone_number_or_email) else null
                })
        }
    }


}