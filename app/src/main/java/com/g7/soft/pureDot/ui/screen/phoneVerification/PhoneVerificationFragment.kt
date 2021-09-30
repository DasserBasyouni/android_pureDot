package com.g7.soft.pureDot.ui.screen.phoneVerification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.databinding.FragmentPhoneVerificationBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale

class PhoneVerificationFragment : Fragment() {
    private lateinit var binding: FragmentPhoneVerificationBinding
    private lateinit var viewModelFactory: PhoneVerificationViewModelFactory
    internal lateinit var viewModel: PhoneVerificationViewModel
    private val args: PhoneVerificationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_phone_verification,
                container,
                false
            )

        viewModelFactory = PhoneVerificationViewModelFactory(
            isPasswordReset = args.isPasswordReset,
            emailOrPhoneNumber = args.emailOrPhoneNumber
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(PhoneVerificationViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observers
        viewModel.verificationResponse.observe(viewLifecycleOwner, {
            viewModel.verificationLcee.value!!.response.value = it

            if (it.validationError == ValidationError.EMPTY_VERIFICATION_CODE)
                binding.squarePinField.error =
                    if (it.validationError == ValidationError.EMPTY_VERIFICATION_CODE)
                        getString(R.string.error_empty_first_name) else null
            else
                if (it.status == ProjectConstant.Companion.Status.SUCCESS && !args.isPasswordReset)
                    binding.actionBtn.performClick()
                else if (it.status == ProjectConstant.Companion.Status.API_ERROR)
                    when (it.apiErrorStatus) {
                        null -> {
                            ProjectDialogUtils.showSimpleMessage(
                                requireContext(),
                                R.string.something_went_wrong,
                                drawableResId = R.drawable.ic_secure_shield
                            )
                        }
                        else -> ProjectDialogUtils.showSimpleMessage(
                            requireContext(),
                            ApiConstant.Status.getMessageResId(it.apiErrorStatus),
                            drawableResId = R.drawable.ic_secure_shield
                        )
                    }
        })

        // setup listeners
        binding.squarePinField.addTextChangedListener {
            if (it?.length == 4)
                viewModel.verify(requireActivity().currentLocale.toLanguageTag())
        }

        binding.actionBtn.setOnClickListener {
            if (args.isPasswordReset) {
                viewModel.changePassword(requireActivity().currentLocale.toLanguageTag())
                    .observeApiResponse(this, { userData ->

                        UserRepository("").saveUserData(
                            requireContext(),
                            userData,
                            viewModel.password.value
                        )

                        ProjectDialogUtils.showSimpleMessage(
                            requireContext(), R.string.msg_password_reset,
                            drawableResId = R.drawable.ic_secure_shield,
                            title = getString(R.string.conc_hello_, userData?.name),
                            positiveBtnTextResId = if (Application.isClientFlavour) R.string.start_shopping else R.string.start_delivery
                        ) {
                            findNavController().navigate(R.id.action_phoneVerificationFragment_to_homeFragment)
                        }
                    })
            } else {
                PhoneVerificationFragmentFlavour().signUpSuccessfulAction(this)
            }
        }

        binding.resendCodeTv.makeLinks(Pair(getString(R.string.resent_code), View.OnClickListener {
            viewModel.resendCode(requireActivity().currentLocale.toLanguageTag())
                .observeApiResponse(
                    this,
                    {
                        viewModel.startCountDownTimer()
                    },
                )
        }), doChangeColor = false)
    }


}