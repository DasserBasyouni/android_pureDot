package com.g7.soft.pureDot.ui.screen.trackOrder

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.OrderTrackingModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class TrackOrderViewModel(val order: OrderModel?) : ViewModel() {

    val orderTrackingLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val orderTrackingResponse = MediatorLiveData<NetworkRequestResponse<OrderTrackingModel>>()


    fun fetchScreenData(langTag: String, tokenId: String) {
        getOrderTracking(langTag, tokenId)
    }

    fun getOrderTracking(langTag: String, tokenId: String) {
        orderTrackingResponse.value = NetworkRequestResponse.loading()

        // fetch request
        orderTrackingResponse.apply {
            addSource(
                OrderRepository(langTag).trackOrder(
                    tokenId = tokenId,
                    orderNumber = order?.number,
                )
            ) { orderTrackingResponse.value = it }
        }
    }

    fun cancelOrder(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).cancelOrder(
                tokenId = tokenId,
                orderNumber = order?.number,
            )
        )
    }


}