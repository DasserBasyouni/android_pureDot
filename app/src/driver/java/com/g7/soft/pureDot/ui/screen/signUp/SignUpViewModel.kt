package com.g7.soft.pureDot.ui.screen.signUp

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class SignUpViewModel : ViewModel() {

    // todo test this format and any other existance in case entering wrong format by user
    val dateFormat = "dd/MM/yyyy"

    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()
    val zipCodesResponse = MediatorLiveData<NetworkRequestResponse<List<ZipCodeModel>?>>()

    val name = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val dateOfBirth = MutableLiveData<String?>()
    val dateOfBirthCalendar =
        MutableLiveData<Calendar?>().apply { this.value = Calendar.getInstance() }

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

    /*val selectedZipCodePosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedZipCode
        get() = zipCodesResponse.value?.data?.getOrNull(
            selectedZipCodePosition.value?.minus(1) ?: -1
        )*/

    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()
    val isMale = MutableLiveData<Boolean?>().apply { this.value = true }
    val carBrand = MutableLiveData<String?>()
    val doAcceptTerms = MutableLiveData<Boolean?>()

    val licenceImagePath = MutableLiveData<String?>()
    val carFrontImagePath = MutableLiveData<String?>()
    val carBackImagePath = MutableLiveData<String?>()
    val nationalIdImagePath = MutableLiveData<String?>()


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

    /*fun getZipCodes(langTag: String) {
        zipCodesResponse.value = NetworkRequestResponse.loading()
        zipCodesResponse.apply {
            this.addSource(
                GeneralRepository(langTag).getZipCodes(
                    cityId = selectedCity?.id
                )
            ) { zipCodesResponse.value = it }
        }
    }*/

    fun register(langTag: String, fcmToken: String) = liveData(Dispatchers.IO) {
        var timestamp: Timestamp? = null

        try {
            val dateFormat = SimpleDateFormat(dateFormat)
            val parsedDate: Date = dateFormat.parse(dateOfBirth.value)
            timestamp = Timestamp(parsedDate.time)
        } catch (e: Exception) {
        }

        // validate inputs
        ValidationUtils().setName(name.value)
            .setPhoneNumber(phoneNumber.value)
            .setEmail(email.value)
            .setCity(selectedCity)
            .setDateOfBirth(timestamp)
            .setCarBrand(carBrand.value)
            .setIsMale(isMale.value)
            .setLicenceImagePath(licenceImagePath.value)
            .setCarFrontImagePath(carFrontImagePath.value)
            .setCarBackImagePath(carBackImagePath.value)
            .setNationalIdImagePath(nationalIdImagePath.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).signUp(
                fcmToken = fcmToken,
                name = name.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                cityId = selectedCity?.id,
                dateOfBirth = timestamp?.time?.div(1000),
                carBrand = carBrand.value,
                isMale = isMale.value,
                licenceImagePath = licenceImagePath.value,
                carFrontImagePath = carFrontImagePath.value,
                carBackImagePath = carBackImagePath.value,
                nationalIdImagePath = nationalIdImagePath.value
            )
        )
    }

}