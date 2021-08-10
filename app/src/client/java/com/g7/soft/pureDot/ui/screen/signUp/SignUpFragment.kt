package com.g7.soft.pureDot.ui.screen.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentSignUpBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_sign_up, container, false)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getCounties(requireActivity().currentLocale.toLanguageTag())
        viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        viewModel.getZipCodes(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.selectedCityPosition.observe(viewLifecycleOwner, {
            viewModel.getZipCodes(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.countiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.countriesSpinner,
                viewModel.countiesResponse.value,
                initialText = getString(R.string.select_country)
            )
        })
        viewModel.citiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.citiesSpinner,
                viewModel.citiesResponse.value,
                initialText = getString(R.string.select_city)
            )
        })
        viewModel.zipCodesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.zipCodeSpinner,
                viewModel.zipCodesResponse.value,
                initialText = getString(R.string.select_zipcode)
            )
        })

        // setup listeners
        binding.acceptTermsCb.makeLinks(
            Pair(
                getString(R.string.terms_and_conditions),
                View.OnClickListener {
                    findNavController().navigate(R.id.termsFragment)
                }),
        )
        binding.registerBtn.setOnClickListener {
            viewModel.register(requireActivity().currentLocale.toLanguageTag()).observeApiResponse(this, {
                // todo save user data
                if (it?.tokenId != null)
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationFragment(
                            false,
                            viewModel.phoneNumber.value
                        )
                    )
                else
                    ProjectDialogUtils.showSimpleMessage(requireContext(), R.string.something_went_wrong, R.drawable.ic_secure_shield)
            })
        }
    }


    private fun <T> setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<T>?>?,
        initialText: String
    ) {
        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

                    }
                    modelsList?.firstOrNull() is ZipCodeModel -> {
                        modelsList.mapNotNull { (it as ZipCodeModel).code }.toTypedArray()
                    }
                    else -> null
                }
                spinner.isEnabled = true
                arrayListOf(initialText).apply {
                    this.addAll((dataList ?: arrayOf()))
                }
            }
            else -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.something_went_wrong))
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(viewModel.selectedCountryPosition.value!!)
        }
    }


}