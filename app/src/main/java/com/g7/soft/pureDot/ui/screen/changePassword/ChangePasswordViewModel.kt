package com.g7.soft.pureDot.ui.screen.changePassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class ChangePasswordViewModel : ViewModel() {
    val currentPassword = MutableLiveData<String?>()
    val newPassword1 = MutableLiveData<String?>()
    val newPassword2 = MutableLiveData<String?>()

    fun changePassword(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .setPassword(currentPassword.value)
            .setPassword(newPassword1.value)
            .setPasswordRepeat(newPassword1.value, newPassword2.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            UserRepository(langTag).changePassword(
                tokenId = tokenId,
                password = newPassword1.value,
                oldPassword = currentPassword.value
            )
        )
    }
}