package com.g7.soft.pureDot.ui.screen.signUp

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.repo.GeneralRepository
import kotlinx.coroutines.Dispatchers

// TODO IMP ADD VALIDATION LAYER ON INPUTS FOR THE WHOLE APP
class SignUpViewModel : ViewModel() {

    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()
    val zipCodesResponse = MediatorLiveData<NetworkRequestResponse<List<ZipCodeModel>?>>()

    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
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

    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()
    val isMale = MutableLiveData<Boolean?>().apply { this.value = true }
    val doAcceptTerms = MutableLiveData<Boolean?>()


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

    fun register(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(
            ClientRepository(langTag).signUp(
                fcmToken = fcmToken,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                cityId = citiesResponse.value?.data?.get(selectedCityPosition.value!!)?.id,
                zipCodeId = zipCodesResponse.value?.data?.get(selectedZipCodePosition.value!!)?.id,
                password = password.value,
                isMale = isMale.value
            )
        )
    }

}