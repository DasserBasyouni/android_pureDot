package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class OrderViewModel(
    internal val masterOrder: MasterOrderModel?,
    internal val masterOrderId: String?
) :
    ViewModel() {

    val orderLcee = MediatorLiveData<LceeModel>().apply {
        this.value = LceeModel().also {
            it.response.value = NetworkRequestResponse.success(data = masterOrder)
        }
    }
    val orderResponse = MediatorLiveData<NetworkRequestResponse<MasterOrderModel>>().apply {
        this.value = NetworkRequestResponse.success(data = masterOrder)
    }

    fun getMasterOrder(langTag: String, tokenId: String?) {
        orderResponse.value = NetworkRequestResponse.loading()

        // fetch request
        orderResponse.apply {
            addSource(
                OrderRepository(langTag).getMasterOrder(
                    tokenId = tokenId,
                    id = masterOrder?.id ?: masterOrderId,
                )
            ) { orderResponse.value = it }
        }
    }

    /*fun cancelOrder(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        /*// validate inputs
        ValidationUtils().setAddressSelectedPosition(selectedAddressPosition.value).getError()
            ?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }*/

        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderId = masterOrder?.id,
                status = ApiConstant.OrderStatus.CANCELED.value,
            )
        )
    }*/

    fun rateOrder(
        langTag: String,
        tokenId: String?,
        orderId: String?,
        orderRating: Int?,
        orderComment: String?,
        deliveryRating: Int?,
        deliveryComment: String?,
    ) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        /*// validate inputs
        ValidationUtils().setAddressSelectedPosition(selectedAddressPosition.value).getError()
            ?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }*/

        emitSource(
            OrderRepository(langTag).rateOrder(
                tokenId = tokenId,
                orderId = orderId,
                orderRating = orderRating,
                orderComment = orderComment,
                deliveryRating = deliveryRating,
                deliveryComment = deliveryComment,
            )
        )
    }


    fun cancelMasterOrder(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).cancelMasterOrder(
                tokenId = tokenId,
                masterOrderId = masterOrder?.id ?: masterOrderId,
            )
        )
    }
}