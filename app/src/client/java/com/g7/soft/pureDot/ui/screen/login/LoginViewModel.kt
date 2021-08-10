package com.g7.soft.pureDot.ui.screen.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ClientRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel : ViewModel() {

    val username = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()

    fun login(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(
            com.g7.soft.pureDot.repo.ClientRepository(langTag).login(
                fcmToken = fcmToken,
                username = username.value,
                password = password.value
            )
        )
    }

}