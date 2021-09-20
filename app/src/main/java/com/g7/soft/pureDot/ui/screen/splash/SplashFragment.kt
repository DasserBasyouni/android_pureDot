package com.g7.soft.pureDot.ui.screen.splash

import android.os.Bundle
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
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

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
                viewModel.login(
                    requireActivity().currentLocale.toLanguageTag(),
                    emailOrPhoneNumber = emailOrPhoneNumber,
                    password = password
                ).observeApiResponse(this@SplashFragment,
                    {
                        UserRepository("").saveUserData(requireContext(), it, password)
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

                    },
                    apiStatusToObserve = arrayOf(
                        ApiConstant.Status.NOT_VERIFIED,
                        ApiConstant.Status.ACCOUNT_NOT_FOUND,
                        ApiConstant.Status.WRONG_PASSWORD,
                    ),
                    chosenApiStatusObserve = {
                        if (it == ApiConstant.Status.NOT_VERIFIED) {
                            val bundle = bundleOf(
                                "isPasswordReset" to false,
                                "emailOrPhoneNumber" to emailOrPhoneNumber
                            )
                            findNavController().navigate(
                                R.id.action_splashFragment_to_phoneVerificationFragment,
                                bundle
                            )
                        } else {
                            UserRepository("").clearUserData(requireContext())
                            findNavController().navigate(R.id.action_splashFragment_to_startFragment)
                        }
                    })
            else
                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
        }
    }
}