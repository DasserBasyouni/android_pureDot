package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class OrderViewModel(val masterOrder: MasterOrderModel?) : ViewModel() {


    fun cancelOrder(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        /*// validate inputs
        ValidationUtils().setAddressSelectedPosition(selectedAddressPosition.value).getError()
            ?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }*/

        emitSource(
            OrderRepository(langTag).cancelOrder(
                tokenId = tokenId,
                orderNumber = masterOrder?.number,
            )
        )
    }

    fun rateOrder(
        langTag: String,
        tokenId: String,
        orderRating: Float?,
        orderComment: String?,
        deliveryRating: Float?,
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
                orderNumber = masterOrder?.number,
                orderRating = orderRating,
                orderComment = orderComment,
                deliveryRating = deliveryRating,
                deliveryComment = deliveryComment,
            )
        )
    }
}