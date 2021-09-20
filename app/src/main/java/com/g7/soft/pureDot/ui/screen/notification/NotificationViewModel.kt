package com.g7.soft.pureDot.ui.screen.notification

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.NotificationModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.NotificationRepository
import kotlinx.coroutines.Dispatchers

class NotificationViewModel : ViewModel() {

    val doNotify = MutableLiveData<Boolean?>()

    val notificationsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val notificationsResponse = MediatorLiveData<NetworkRequestResponse<List<NotificationModel>>>()


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