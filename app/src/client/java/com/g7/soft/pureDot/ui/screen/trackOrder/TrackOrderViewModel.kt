package com.g7.soft.pureDot.ui.screen.trackOrder

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.OrderTrackingModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class TrackOrderViewModel(
    val orderId: String?,
    val orderNumber: Int?
) : ViewModel() {

    val orderTrackingLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val orderTrackingResponse = MediatorLiveData<NetworkRequestResponse<List<OrderTrackingModel>>>()

    val isDelivered = OrderModel(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        status = orderTrackingResponse.value?.data?.last()?.status,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    ).isDelivered

    val isCancelable = OrderModel(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        status = orderTrackingResponse.value?.data?.last()?.status,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    ).isCancelable

    fun fetchScreenData(langTag: String, tokenId: String?) {
        getOrderTracking(langTag, tokenId)
    }

    fun getOrderTracking(langTag: String, tokenId: String?) {
        orderTrackingResponse.value = NetworkRequestResponse.loading()

        // fetch request
        orderTrackingResponse.apply {
            addSource(
                OrderRepository(langTag).trackOrder(
                    tokenId = tokenId,
                    orderId = orderId,
                )
            ) { orderTrackingResponse.value = it }
        }
    }

    fun cancelOrder(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderId = orderId,
                status = ApiConstant.OrderStatus.CANCELED.value,
                isReturn = null,
            )
        )
    }


}