package com.g7.soft.pureDot.ui.screen.profileEdit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

// TODO IMP ADD VALIDATION LAYER ON INPUTS FOR THE WHOLE APP
class ProfileEditViewModel(
    val userData: UserDataModel?,
    val signUpFields: SignUpFieldsModel?
) : ViewModel() {

    val dateFormat = "dd/MM/yyyy"


    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>?>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()
    val zipCodesResponse = MediatorLiveData<NetworkRequestResponse<List<ZipCodeModel>?>>()

    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val dateOfBirth = MutableLiveData<String?>()
    val dateOfBirthCalendar =
        MutableLiveData<Calendar?>().apply { this.value = Calendar.getInstance() }
    val selectedGenderPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val isMale
        get() = when (selectedGenderPosition.value) {
            1 -> true
            2 -> false
            else -> null
        }

    val selectedCountryPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCountry
        get() = countiesResponse.value?.data?.getOrNull(
            selectedCountryPosition.value?.minus(1) ?: -1
        )

    val selectedCityPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCity
        get() = citiesResponse.value?.data?.getOrNull(
            selectedCityPosition.value?.minus(1) ?: -1
        )

    val selectedZipCodePosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedZipCode
        get() = zipCodesResponse.value?.data?.getOrNull(
            selectedZipCodePosition.value?.minus(1) ?: -1
        )

    init {
        firstName.value = userData?.firstName
        lastName.value = userData?.lastName
        phoneNumber.value = userData?.phoneNumber
        email.value = userData?.email

        dateOfBirth.value = userData?.dateOfBirth?.toFormattedDateTime(dateFormat)
        selectedGenderPosition.value = if (userData?.isMale == true) 0 else 1
    }


    fun fetchData(langTag: String) {
        getCounties(langTag)
        //getCities(langTag)
        //getZipCodes(langTag)
    }

    fun getCounties(langTag: String) {
        countiesResponse.value = NetworkRequestResponse.loading()
        countiesResponse.apply {
            this.addSource(GeneralRepository(langTag).getCounties()) { countiesResponse.value = it }
        }
    }

    fun getCities(langTag: String) {
        citiesResponse.value = NetworkRequestResponse.loading()
        citiesResponse.apply {
            this.addSource(
                GeneralRepository(langTag).getCities(
                    countryId = selectedCountry?.id
                )
            ) { citiesResponse.value = it }
        }
    }

    fun getZipCodes(langTag: String) {
        zipCodesResponse.value = NetworkRequestResponse.loading()
        zipCodesResponse.apply {
            this.addSource(
                GeneralRepository(langTag).getZipCodes(
                    cityId = selectedCity?.id
                )
            ) { zipCodesResponse.value = it }
        }
    }

    fun save(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {

        var timestamp: Timestamp? = null

        try {
            val dateFormat = SimpleDateFormat(dateFormat)
            val parsedDate: Date = dateFormat.parse(dateOfBirth.value)
            timestamp = Timestamp(parsedDate.time)
        } catch (e: Exception) {
        }

        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils().setFirstName(firstName.value)
            .setLastName(lastName.value)
            .setPhoneNumber(phoneNumber.value)
            .setEmail(email.value)
            .setCity(selectedCity)
            .setZipCode(selectedZipCode, signUpFields?.haveZipCode)
            .setIsMale(isMale)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            UserRepository(langTag).editUserData(
                tokenId = tokenId,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                cityId = selectedCity?.id,
                zipCodeId = if (signUpFields?.haveZipCode == true) selectedZipCode?.id else null,
                isMale = selectedGenderPosition.value == 0,
                dateOfBirth = timestamp?.time?.div(1000),
                imageUrl = null //todo
            )
        )
    }

}