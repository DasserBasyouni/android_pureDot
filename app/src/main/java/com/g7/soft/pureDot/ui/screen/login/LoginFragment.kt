package com.g7.soft.pureDot.ui.screen.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentLoginBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale

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
            viewModel.login(requireActivity().currentLocale.toLanguageTag())
                .observeApiResponse(this, {
                    if (it?.tokenId != null) {
                        Log.e("Z_", "here?!")
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        //findNavController().navigate(R.id.newHomeFragment)
                    } else
                        ProjectDialogUtils.showSimpleMessage(
                            requireContext(),
                            R.string.something_went_wrong,
                            R.drawable.ic_secure_shield
                        )
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
                getString(R.string.register),
                View.OnClickListener {
                    findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                }), doChangeColor = false, isFakeBoldText = true
        )

    }


}