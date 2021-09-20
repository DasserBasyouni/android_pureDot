package com.g7.soft.pureDot.ui.screen.profileEdit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.CountryModel
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

// TODO IMP ADD VALIDATION LAYER ON INPUTS FOR THE WHOLE APP
class ProfileEditViewModel(val userData: UserDataModel?) : ViewModel() {

    val dateFormat = "dd/MM/yyyy"


    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>?>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()

    val name = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val dateOfBirth = MutableLiveData<String?>()
    val dateOfBirthCalendar =
        MutableLiveData<Calendar?>().apply { this.value = Calendar.getInstance() }
    val selectedGenderPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCountryPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedCityPosition = MutableLiveData<Int?>().apply { this.value = 0 }


    init {
        name.value = userData?.name
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

    fun save(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {

        var timestamp: Timestamp? = null

        try {
            val dateFormat = SimpleDateFormat(dateFormat)
            val parsedDate: Date = dateFormat.parse(dateOfBirth.value)
            timestamp = Timestamp(parsedDate.time)
        } catch (e: Exception) {
        }

        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).editUserData(
                tokenId = tokenId,
                name = name.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                cityId = citiesResponse.value?.data?.get(selectedCityPosition.value!!)?.id,
                isMale = selectedGenderPosition.value == 0,
                dateOfBirth = timestamp?.time?.div(1000),
                imageUrl = null //todo
            )
        )
    }

}