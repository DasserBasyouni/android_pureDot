package com.g7.soft.pureDot.ui.screen.contactUs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import kotlinx.coroutines.Dispatchers

class ContactUsViewModel : ViewModel() {

    val name = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val message = MutableLiveData<String?>()


    fun submit(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            GeneralRepository(langTag).contactUs(
                name = name.value,
                email = email.value,
                message = message.value,
            )
        )
    }
}