package com.g7.soft.pureDot.ui.screen.profile

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.DriverDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.DriverRepository

class ProfileViewModel : ViewModel() {

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<DriverDataModel?>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getUserData(langTag: String, tokenId: String) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(DriverRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
            }
        }
    }

}