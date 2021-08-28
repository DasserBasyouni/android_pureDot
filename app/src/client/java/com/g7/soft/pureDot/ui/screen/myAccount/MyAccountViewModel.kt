package com.g7.soft.pureDot.ui.screen.myAccount

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.ClientDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ClientRepository
import kotlinx.coroutines.Dispatchers

class MyAccountViewModel : ViewModel() {

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<ClientDataModel>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getUserData(langTag: String, tokenId: String) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(ClientRepository(langTag).getLocalUserData(tokenId = tokenId)) {
                userDataResponse.value = it
            }
        }
    }

    fun logout(langTag: String) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(
            ClientRepository(langTag).logout(
                fcmToken = fcmToken
            )
        )
    }

}