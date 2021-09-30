package com.g7.soft.pureDot.ui.screen.profileEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentProfileEditBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ProfileEditFragment : Fragment() {

    internal lateinit var binding: FragmentProfileEditBinding
    private lateinit var viewModelFactory: ProfileEditViewModelFactory
    internal lateinit var viewModel: ProfileEditViewModel
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

        viewModelFactory = ProfileEditFragmentFlavour().getViewModelFactory(args)
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

        viewModel.fetchData(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.selectedCountryPosition.observe(viewLifecycleOwner, {
            viewModel.getCities(requireActivity().currentLocale.toLanguageTag())
        })
        viewModel.countiesResponse.observe(viewLifecycleOwner, {
            ProfileEditFragmentFlavour().setupSpinner(
                this,
                binding.countriesSpinner,
                viewModel.countiesResponse.value,
                initialText = getString(R.string.select_country)
            )
            viewModel.selectedCountryPosition.value =
                (it?.data?.indexOfFirst { country -> viewModel.userData?.country?.id == country.id })
                    ?.plus(1)
        })
        viewModel.citiesResponse.observe(viewLifecycleOwner, {
            ProfileEditFragmentFlavour().setupSpinner(
                this,
                binding.citiesSpinner,
                viewModel.citiesResponse.value,
                initialText = getString(R.string.select_city)
            )
            viewModel.selectedCityPosition.value =
                (it.data?.indexOfFirst { city -> viewModel.userData?.city?.id == city.id })?.plus(1)
        })
        ProfileEditFragmentFlavour().observe(this)

        // setup listeners
        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.save(requireActivity().currentLocale.toLanguageTag(), tokenId)
                    .observeApiResponse(this@ProfileEditFragment, {
                        findNavController().popBackStack()
                    }, validationObserve = {
                        ProfileEditFragmentFlavour().saveValidationObserve(this@ProfileEditFragment, it)

                        binding.phoneNumberTil.error =
                            if (it == ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER)
                                getString(R.string.error_empty_phone_number) else null

                        binding.emailTil.error = when (it) {
                            ProjectConstant.Companion.ValidationError.EMPTY_EMAIL -> getString(R.string.error_empty_email)
                            ProjectConstant.Companion.ValidationError.INVALID_EMAIL -> getString(R.string.error_invalid_email)
                            else -> null
                        }

                        binding.birthDateTil.error =
                            if (it == ProjectConstant.Companion.ValidationError.EMPTY_DATE_OF_BIRTH)
                                getString(R.string.error_empty_date_of_birth) else null
                    })
            }
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        ProfileEditFragmentFlavour().onClickListeners(this)
    }

}