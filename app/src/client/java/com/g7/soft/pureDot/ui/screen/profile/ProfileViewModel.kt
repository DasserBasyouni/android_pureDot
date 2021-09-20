package com.g7.soft.pureDot.ui.screen.profile

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.model.SignUpFieldsModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.repo.GeneralRepository

class ProfileViewModel : ViewModel() {

    val signUpFieldsResponse = MediatorLiveData<NetworkRequestResponse<SignUpFieldsModel?>>()
    val signUpFieldsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<UserDataModel?>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getUserData(langTag: String, tokenId: String?) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(UserRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
            }
        }
    }

    fun getSignUpFields(langTag: String) {
        signUpFieldsResponse.value = NetworkRequestResponse.loading()
        signUpFieldsResponse.apply {
            this.addSource(GeneralRepository(langTag).getSignUpFields()) {
                signUpFieldsResponse.value = it
            }
        }
    }
}