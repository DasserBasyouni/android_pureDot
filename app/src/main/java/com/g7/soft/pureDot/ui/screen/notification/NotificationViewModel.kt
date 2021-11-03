package com.g7.soft.pureDot.ui.screen.notification

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.NotificationModel
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.NotificationRepository
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers

class NotificationViewModel : ViewModel() {

    val doNotify = MutableLiveData<Boolean?>()

    val notificationsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val notificationsResponse = MediatorLiveData<NetworkRequestResponse<List<NotificationModel>>>()

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

    fun getNotifications(langTag: String, tokenId: String?) {
        notificationsResponse.value = NetworkRequestResponse.loading()

        // fetch request
        notificationsResponse.apply {
            addSource(
                NotificationRepository(langTag).getNotifications(
                    tokenId = tokenId,
                )
            ) { notificationsResponse.value = it }
        }
    }

    fun doNotify(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            NotificationRepository(langTag).doNotify(
                tokenId = tokenId,
                doNotify = doNotify.value
            )
        )
    }

}