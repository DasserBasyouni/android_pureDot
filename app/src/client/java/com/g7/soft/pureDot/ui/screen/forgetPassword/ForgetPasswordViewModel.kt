package com.g7.soft.pureDot.ui.screen.forgetPassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class ForgetPasswordViewModel : ViewModel() {

    val emailOrPhoneNumber = MutableLiveData<String?>()

    fun forgetPassword(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        Log.e("Z_", "loading?")

        // validate inputs
        ValidationUtils()
            .setPhoneNumberOrEmail(emailOrPhoneNumber.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            UserRepository(langTag).sendForgetPasswordCode(
                emailOrPhoneNumber = emailOrPhoneNumber.value
            )
        )
    }

}