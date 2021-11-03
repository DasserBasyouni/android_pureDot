package com.g7.soft.pureDot.ui.screen.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentSplashBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch
import timber.log.Timber

// todo make this fragment MVVM arch
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_splash, container, false)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAppCurrency(requireActivity().currentLocale.toLanguageTag())
            .observeApiResponse(
                this, {
                    UserRepository("").updateCurrencySymbol(requireContext(), it?.name)
                    binding.root.post(this::animateToDesiredScreen)
                }
            )
    }

    private fun animateToDesiredScreen() {
        lifecycleScope.launch {
            val userRepo = UserRepository("")
            val tokenId = userRepo.getTokenId(requireContext())
            val isGuestAccount = userRepo.getIsGuestAccount(requireContext())
            val emailOrPhoneNumber = userRepo.getEmailOrPhoneNumber(requireContext())
            val password = userRepo.getPassword(requireContext())

            if (!tokenId.isNullOrEmpty() || isGuestAccount)
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.w("Fetching FCM registration token failed")
                        Timber.w(task.exception)
                        return@OnCompleteListener
                    }

                    val fcmToken = task.result

                    viewModel.login(
                        requireActivity().currentLocale.toLanguageTag(),
                        emailOrPhoneNumber = emailOrPhoneNumber,
                        password = password,
                        fcmToken = fcmToken
                    ).observeApiResponse(this@SplashFragment,
                        {
                            UserRepository("").saveUserData(requireContext(), it, password)
                            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                            Log.e("Z_", "userTokenId: ${it?.tokenId}")
                        },
                        apiStatusToObserve = arrayOf(
                            ApiConstant.Status.NOT_VERIFIED,
                            ApiConstant.Status.ACCOUNT_NOT_FOUND,
                            ApiConstant.Status.WRONG_PASSWORD,
                        ),
                        chosenApiStatusObserve = { status, _ ->
                            if (status == ApiConstant.Status.NOT_VERIFIED) {
                                val bundle = bundleOf(
                                    "isPasswordReset" to false,
                                    "isWalletVerification" to false,
                                    "emailOrPhoneNumber" to emailOrPhoneNumber
                                )
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_phoneVerificationFragment,
                                    bundle
                                )
                            } else {
                                UserRepository("").clearUserData(lifecycleScope, requireContext())
                                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
                            }
                        })
                })
            else
                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
        }
    }
}