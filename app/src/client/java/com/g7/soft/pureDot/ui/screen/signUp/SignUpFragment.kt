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
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.databinding.FragmentSignUpBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.zeugmasolutions.localehelper.currentLocale
import timber.log.Timber

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
        viewModel.fetchData(requireActivity().currentLocale.toLanguageTag())

        // observables
        viewModel.signUpFieldsResponse.observe(viewLifecycleOwner, {
            viewModel.signUpFieldsLcee.value!!.response.value = it
        })
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
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed")
                    Timber.w(task.exception)
                    return@OnCompleteListener
                }

                val fcmToken = task.result

                viewModel.register(requireActivity().currentLocale.toLanguageTag(), fcmToken)
                    .observeApiResponse(this, {
                        if (it != null) {
                            // ClientRepository("").saveUserData(requireContext(), it, viewModel.password.value) todo
                            // save user data
                            UserRepository("").updateIsGuestAccount(requireContext(), false)
                            UserRepository("").updateTokenId(requireContext(), it)

                            findNavController().navigate(
                                SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationFragment(
                                    false,
                                    viewModel.phoneNumber.value
                                )
                            )
                        } else
                            ProjectDialogUtils.showSimpleMessage(
                                requireContext(),
                                R.string.something_went_wrong,
                                drawableResId = R.drawable.ic_secure_shield
                            )
                    }, validationObserve = {
                        binding.firstNameTil.error = if (it == ValidationError.EMPTY_FIRST_NAME)
                            getString(R.string.error_empty_first_name) else null

                        binding.lastNameTil.error = if (it == ValidationError.EMPTY_LAST_NAME)
                            getString(R.string.error_empty_last_name) else null

                        binding.phoneNumberTil.error = if (it == ValidationError.EMPTY_PHONE_NUMBER)
                            getString(R.string.error_empty_phone_number) else null

                        binding.emailTil.error = when (it) {
                            ValidationError.EMPTY_EMAIL -> getString(R.string.error_empty_email)
                            ValidationError.INVALID_EMAIL -> getString(R.string.error_invalid_email)
                            else -> null
                        }

                        binding.passwordTil.error = when (it) {
                            ValidationError.EMPTY_PASSWORD -> getString(R.string.error_empty_password)
                            ValidationError.INVALID_PASSWORD -> getString(R.string.error_invalid_password)
                            else -> null
                        }

                        binding.confirmPasswordTil.error =
                            if (it == ValidationError.NON_IDENTICAL_PASSWORD)
                                getString(R.string.error_non_identical_passwords) else null
                    })
            })
        }
    }


    private fun <T> setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<T>?>?,
        initialText: String
    ) {
        var selectedPosition = 0

        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS, ProjectConstant.Companion.Status.API_ERROR -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        selectedPosition = viewModel.selectedCountryPosition.value!!
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        selectedPosition = viewModel.selectedCityPosition.value!!
                        modelsList.mapNotNull { (it as CityModel).name }.toTypedArray()

                    }
                    modelsList?.firstOrNull() is ZipCodeModel -> {
                        selectedPosition = viewModel.selectedZipCodePosition.value!!
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
            spinner.setSelection(selectedPosition)
        }
    }

}