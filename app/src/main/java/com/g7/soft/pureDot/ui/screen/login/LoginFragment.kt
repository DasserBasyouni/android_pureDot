package com.g7.soft.pureDot.ui.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.databinding.FragmentLoginBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.zeugmasolutions.localehelper.currentLocale
import timber.log.Timber

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.isShopOwnerApp = Application.isShopOwnerFlavour
        binding.isClientApp = Application.isClientFlavour
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup listeners
        binding.loginBtn.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed")
                    Timber.w(task.exception)
                    return@OnCompleteListener
                }

                val fcmToken = task.result

                viewModel.login(requireActivity().currentLocale.toLanguageTag(), fcmToken)
                    .observeApiResponse(this, {
                        if (it?.tokenId != null) {
                            UserRepository("").saveUserData(
                                requireContext(),
                                it,
                                viewModel.password.value
                            )

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else
                            ProjectDialogUtils.showSimpleMessage(
                                requireContext(),
                                messageResId = R.string.something_went_wrong,
                                drawableResId = R.drawable.ic_secure_shield
                            )
                    }, validationObserve = {
                        binding.emailOrPhoneNumberTil.error =
                            if (it == ValidationError.EMPTY_PHONE_NUMBER_OR_EMAIL)
                                getString(R.string.error_empty_phone_number_or_email) else null

                        binding.passwordTil.error = when (it) {
                            ValidationError.EMPTY_PASSWORD -> getString(R.string.error_empty_password)
                            ValidationError.INVALID_PASSWORD -> getString(R.string.error_invalid_password)
                            else -> null
                        }
                    },
                        apiStatusToObserve = arrayOf(ApiConstant.Status.NOT_VERIFIED),
                        chosenApiStatusObserve = { _, _ ->
                            val bundle = bundleOf(
                                "isPasswordReset" to false,
                                "isWalletVerification" to false,
                                "emailOrPhoneNumber" to viewModel.emailOrPhoneNumber.value
                            )
                            findNavController().navigate(
                                R.id.action_loginFragment_to_phoneVerificationFragment, bundle
                            )

                        })
            })
        }
        binding.forgetPasswordTv.makeLinks(
            Pair(
                getString(R.string.forget_password),
                View.OnClickListener {
                    findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
                }), doChangeColor = false
        )
        binding.registerTextTv.makeLinks(
            Pair(
                getString(R.string.part_register),
                View.OnClickListener {
                    findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                }), doChangeColor = false, isFakeBoldText = true
        )

    }


}