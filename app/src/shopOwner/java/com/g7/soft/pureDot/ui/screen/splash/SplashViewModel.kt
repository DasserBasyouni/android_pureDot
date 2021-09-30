package com.g7.soft.pureDot.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers

class SplashViewModel : ViewModel() {

    fun getAppCurrency(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(GeneralRepository(langTag).getAppCurrency())
    }

    fun login(langTag: String, emailOrPhoneNumber: String?, password: String?, fcmToken: String) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())
            emitSource(
                UserRepository(langTag).login(
                    fcmToken = fcmToken,
                    emailOrPhoneNumber = emailOrPhoneNumber,
                    password = password
                )
            )
        }
}