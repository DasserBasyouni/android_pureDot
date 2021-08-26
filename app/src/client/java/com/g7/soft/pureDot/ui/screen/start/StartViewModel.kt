package com.g7.soft.pureDot.ui.screen.start

import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    // todo make it without network request
    /*fun signUpAsGuest(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(ClientRepository(langTag).signUpAsGuest(fcmToken = fcmToken))
    }*/

}