package com.g7.soft.pureDot.ui.screen.settings

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
import com.g7.soft.pureDot.adapter.SettingsAdapter
import com.g7.soft.pureDot.databinding.FragmentSettingsBinding
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale

class MyAccountFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    internal lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" // todo
        viewModel.getUserData(requireActivity().currentLocale.toLanguageTag(), tokenId)

        // setup observers
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it
        })

        // setup settings
        binding.settingsRv.adapter = SettingsAdapter(this)

        // add decoration divider
        binding.settingsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.profileCv.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

}