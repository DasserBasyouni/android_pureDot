package com.g7.soft.pureDot.ui.screen.changePassword

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
import com.g7.soft.pureDot.databinding.FragmentChangePasswordBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.ClientRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    internal lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_change_password,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup onClick
        binding.changePasswordBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    ClientRepository("").getLocalUserData(requireContext()).tokenId

                viewModel.changePassword(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                ).observeApiResponse(this@ChangePasswordFragment, {
                    findNavController().popBackStack()
                })
            }
        }
    }

}