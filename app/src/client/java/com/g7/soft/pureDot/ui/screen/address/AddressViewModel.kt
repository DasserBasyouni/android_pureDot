package com.g7.soft.pureDot.ui.screen.address

import android.content.Context
import android.location.Location
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
import kotlinx.coroutines.Dispatchers

class AddressViewModel : ViewModel() {

    var location: MutableLiveData<Location>? = MutableLiveData<Location>()
    var locationRepository: LocationListener? = null

    val countiesResponse = MediatorLiveData<NetworkRequestResponse<List<CountryModel>?>>()
    val citiesResponse = MediatorLiveData<NetworkRequestResponse<List<CityModel>?>>()
    val zipCodesResponse = MediatorLiveData<NetworkRequestResponse<List<ZipCodeModel>?>>()

    val flat = MutableLiveData<String?>()
    val floor = MutableLiveData<String?>()
    val buildingNumber = MutableLiveData<String?>()
    val streetName = MutableLiveData<String?>()
    val areaName = MutableLiveData<String?>()
    val isMainAddress = MutableLiveData<Boolean?>().apply { this.value = true }

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


    fun fetchData(langTag: String) {
        getCounties(langTag)
        getCities(langTag)
        getZipCodes(langTag)
    }

    fun setLocationRepository(context: Context) {
        locationRepository = LocationListener.getInstance(context)
    }

    fun enableLocationServices(){
        locationRepository?.let {
            it.startService()
        }
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

    fun addAddress(langTag: String, tokenId: String?, latitude: Double?, longitude: Double?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        /*ValidationUtils().setCountry(country.value).setCity(city.value).setArea(area.value)
            .setZipCode(zipCode.value).setAddress(address.value).getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }*/

        // fetch request
        emitSource(
            UserRepository(langTag).addAddress(
                tokenId = tokenId,
                flat = flat.value,
                floor = floor.value,
                buildingNumber = buildingNumber.value,
                streetName = streetName.value,
                area = areaName.value,
                isMainAddress = isMainAddress.value,
                cityId = selectedCity?.id,
                latitude = latitude,
                longitude = longitude,
                zipCodeId =  selectedZipCode?.id,
            ))
    }

}