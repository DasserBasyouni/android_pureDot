package com.g7.soft.pureDot.ui.screen.address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ClientRepository
import kotlinx.coroutines.Dispatchers

class AddressViewModel : ViewModel() {


    val flat = MutableLiveData<String?>()
    val floor = MutableLiveData<String?>()
    val homeNumber = MutableLiveData<String?>()
    val streetName = MutableLiveData<String?>()
    val area = MutableLiveData<String?>()
    val isMainAddress = MutableLiveData<Boolean?>().apply { this.value = true }


    fun addAddress(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        /*ValidationUtils().setCountry(country.value).setCity(city.value).setArea(area.value)
            .setZipCode(zipCode.value).setAddress(address.value).getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }*/

        // fetch request
        emitSource(
            ClientRepository(langTag).addAddress(
                tokenId = tokenId,
                flat = flat.value,
                floor = floor.value,
                homeNumber = homeNumber.value,
                streetName = streetName.value,
                area = area.value,
                isMainAddress = isMainAddress.value,
            )
        )
    }

}