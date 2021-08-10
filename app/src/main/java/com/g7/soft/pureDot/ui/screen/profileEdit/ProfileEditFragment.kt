package com.g7.soft.pureDot.ui.screen.profileEdit

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
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentProfileEditBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.zeugmasolutions.localehelper.currentLocale

class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private lateinit var viewModelFactory: ProfileEditViewModelFactory
    private lateinit var viewModel: ProfileEditViewModel
    private val args: ProfileEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_profile_edit,
                container,
                false
            )

        viewModelFactory = ProfileEditViewModelFactory(
            userData = args.userData,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileEditViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup gender spinner
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.male), getString(R.string.female))
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderSpinner.adapter = adapter
            binding.genderSpinner.setSelection(viewModel.selectedGenderPosition.value!!)
        }

        // fetch data
        viewModel.getCounties(requireActivity().currentLocale.toLanguageTag())
        viewModel.getCities(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.countiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.countriesSpinner,
                viewModel.countiesResponse.value,
                initialText = getString(R.string.select_country)
            )
            viewModel.selectedCountryPosition.value =
                (it?.data?.indexOfFirst { country -> viewModel.userData?.country?.id == country.id })
                    ?.plus(1)
        })
        viewModel.citiesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.citiesSpinner,
                viewModel.citiesResponse.value,
                initialText = getString(R.string.select_city)
            )
            viewModel.selectedCityPosition.value =
                (it.data?.indexOfFirst { city -> viewModel.userData?.city?.id == city.id })?.plus(1)
        })

        // setup listeners
        binding.saveBtn.setOnClickListener {
            val tokenId = "" // todo
            viewModel.save(requireActivity().currentLocale.toLanguageTag(), tokenId)
                .observeApiResponse(this, {
                    findNavController().popBackStack()
                })
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun <T> setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<T>?>?,
        initialText: String
    ) {
        var selectionPosition = 0
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
                        selectionPosition = viewModel.selectedCountryPosition.value!!
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        selectionPosition = viewModel.selectedCityPosition.value!!
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

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
            spinner.setSelection(selectionPosition)
        }
    }


}