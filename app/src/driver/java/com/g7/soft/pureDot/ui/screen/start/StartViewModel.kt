package com.g7.soft.pureDot.ui.screen.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.DriverRepository
import kotlinx.coroutines.Dispatchers

class StartViewModel : ViewModel() {

    fun signUpAsGuest(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(DriverRepository(langTag).signUpAsGuest(fcmToken = fcmToken))
    }

}