package com.g7.soft.pureDot.ui.screen.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.util.ValidationUtils
import kotlinx.coroutines.Dispatchers

class LoginViewModel : ViewModel() {

    val emailOrPhoneNumber = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()

    fun login(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        // validate inputs
        ValidationUtils()
            .setPhoneNumberOrEmail(emailOrPhoneNumber.value)
            .setPassword(password.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).login(
                fcmToken = fcmToken,
                emailOrPhoneNumber = emailOrPhoneNumber.value,
                password = password.value
            )
        )
    }

}