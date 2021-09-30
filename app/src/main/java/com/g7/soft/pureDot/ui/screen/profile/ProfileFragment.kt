package com.g7.soft.pureDot.ui.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentProfileBinding
import com.g7.soft.pureDot.ui.DividerItemDecorator

class ProfileFragment : Fragment() {
    internal lateinit var binding: FragmentProfileBinding
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

        ProfileFragmentFlavour().fetchData(this)

        // setup observers
        ProfileFragmentFlavour().observe(this)
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it
            ProfileFragmentFlavour().setupAdapters(this, it)
        })

        // add decoration divider
        binding.settingsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(
                R.id.profileEditFragment,
                ProfileFragmentFlavour().getProfileEditBundle(viewModel)
            )
        }
        binding.root.findViewById<View>(R.id.bankAccountTv)?.setOnClickListener {
            findNavController().navigate(R.id.bankAccountFragment)
        }
        binding.root.findViewById<View>(R.id.workingHoursTv)?.setOnClickListener {
            findNavController().navigate(R.id.workingHoursFragment)
        }
    }
}