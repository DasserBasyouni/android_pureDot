package com.g7.soft.pureDot.ui.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProfileAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentProfileBinding
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    internal lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSignUpFields(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        viewModel.signUpFieldsResponse.observe(viewLifecycleOwner, {
            viewModel.signUpFieldsLcee.value!!.response.value = it

            if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                // fetch data
                lifecycleScope.launch {
                    val tokenId =
                        ClientRepository("").getLocalUserData(requireContext()).tokenId

                    viewModel.getUserData(requireActivity().currentLocale.toLanguageTag(), tokenId)
                }
            }
        })
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it
            binding.settingsRv.adapter =
                ProfileAdapter(this, it.data, viewModel.signUpFieldsResponse.value?.data)
        })

        // add decoration divider
        binding.settingsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.editProfileBtn.setOnClickListener {
            val bundle = bundleOf(
                "userData" to viewModel.userDataResponse.value?.data,
                "signUpFields" to viewModel.signUpFieldsResponse.value?.data
            )
            findNavController().navigate(R.id.profileEditFragment, bundle)
        }
        binding.root.findViewById<View>(R.id.bankAccountTv)?.setOnClickListener {
            findNavController().navigate(R.id.bankAccountFragment)
        }
        binding.root.findViewById<View>(R.id.workingHoursTv)?.setOnClickListener {
            findNavController().navigate(R.id.workingHoursFragment)
        }
    }
}