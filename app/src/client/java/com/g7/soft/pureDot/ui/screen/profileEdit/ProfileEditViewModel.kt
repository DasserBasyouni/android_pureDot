package com.g7.soft.pureDot.ui.screen.profileEdit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.ClientDataModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.repo.GeneralRepository
import kotlinx.coroutines.Dispatchers
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

// TODO IMP ADD VALIDATION LAYER ON INPUTS FOR THE WHOLE APP
class ProfileEditViewModel(val userData: ClientDataModel?) : ViewModel() {

    val dateFormat = "dd/MM/yyyy"


    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>?>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()

    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val dateOfBirth = MutableLiveData<String?>()
    val selectedGenderPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCountryPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCityPosition = MutableLiveData<Int?>().apply { this.value = 0 }


    init {
        firstName.value = userData?.firstName
        lastName.value = userData?.lastName
        phoneNumber.value = userData?.phoneNumber
        email.value = userData?.email

        dateOfBirth.value = userData?.dateOfBirth?.toFormattedDateTime(dateFormat)
        selectedGenderPosition.value = if (userData?.isMale == true) 0 else 1
    }


    fun getCounties(langTag: String) {
        countiesResponse.value = NetworkRequestResponse.loading()
        countiesResponse.apply {
            this.addSource(GeneralRepository(langTag).getCounties()) {
                countiesResponse.value = it
            }
        }
    }

    fun getCities(langTag: String) {
        citiesResponse.value = NetworkRequestResponse.loading()
        citiesResponse.apply {
            this.addSource(
                GeneralRepository(langTag).getCities(
                    countryId = countiesResponse.value?.data?.get(
                        selectedCountryPosition.value!!
                    )?.id
                )
            ) {
                citiesResponse.value = it
            }
        }
    }

    fun save(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {

        var timestamp: Timestamp? = null

        try {
            val dateFormat = SimpleDateFormat(dateFormat)
            val parsedDate: Date = dateFormat.parse(dateOfBirth.value)
            timestamp = Timestamp(parsedDate.time)
        } catch (e: Exception) {
        }

        emit(NetworkRequestResponse.loading())
        emitSource(
            ClientRepository(langTag).editUserData(
                tokenId = tokenId,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                countryId = countiesResponse.value?.data?.get(selectedCountryPosition.value!!)?.id,
                cityId = citiesResponse.value?.data?.get(selectedCityPosition.value!!)?.id,
                isMale = selectedGenderPosition.value == 0,
                countryCode = null,
                dateOfBirth = timestamp?.time?.div(1000),
                imageUrl = null //todo
            )
        )
    }

}