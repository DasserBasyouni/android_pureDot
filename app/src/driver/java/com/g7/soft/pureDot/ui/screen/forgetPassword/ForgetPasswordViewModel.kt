package com.g7.soft.pureDot.ui.screen.forgetPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.DriverRepository
import kotlinx.coroutines.Dispatchers

class ForgetPasswordViewModel : ViewModel() {

    val emailOrPhoneNumber = MutableLiveData<String?>()

    fun forgetPassword(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            DriverRepository(langTag).sendForgetPasswordCode(
                emailOrPhoneNumber = emailOrPhoneNumber.value
            )
        )
    }

}