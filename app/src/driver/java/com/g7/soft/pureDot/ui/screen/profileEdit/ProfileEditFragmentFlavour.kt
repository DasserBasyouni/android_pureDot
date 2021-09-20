package com.g7.soft.pureDot.ui.screen.profileEdit

import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.zeugmasolutions.localehelper.currentLocale
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditFragmentFlavour {
    fun getViewModelFactory(args: ProfileEditFragmentArgs) = ProfileEditViewModelFactory(
        userData = args.userData
    )

    fun observe(fragment: ProfileEditFragment) = Unit


    fun <T> setupSpinner(
        fragment: ProfileEditFragment,
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
                arrayListOf(fragment.getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS, ProjectConstant.Companion.Status.API_ERROR -> {
                val modelsList = networkResponse.data
                val dataList = when {
                    modelsList?.firstOrNull() is CountryModel -> {
                        selectedPosition = fragment.viewModel.selectedCountryPosition.value!!
                        modelsList.mapNotNull { (it as CountryModel).name }.toTypedArray()
                    }
                    modelsList?.firstOrNull() is CityModel -> {
                        selectedPosition = fragment.viewModel.selectedCityPosition.value!!
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
                arrayListOf(fragment.getString(R.string.something_went_wrong))
            }
        }

        ArrayAdapter(
            fragment.requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(selectedPosition)
        }
    }

    internal fun onClickListeners(fragment: ProfileEditFragment){
        fragment.binding.birthDateTil.editText?.setOnClickListener {
            val calendar = fragment.viewModel.dateOfBirthCalendar.value!!
            DatePickerDialog(
                fragment.requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    fragment.viewModel.dateOfBirthCalendar.value?.set(Calendar.YEAR, year)
                    fragment.viewModel.dateOfBirthCalendar.value?.set(Calendar.MONTH, monthOfYear)
                    fragment.viewModel.dateOfBirthCalendar.value?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    fragment.viewModel.dateOfBirth.value = SimpleDateFormat(
                        fragment.getString(R.string.format_standard_date),
                        fragment.requireActivity().currentLocale
                    ).format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}