package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class OrderViewModel(val masterOrder: MasterOrderModel?) : ViewModel() {


    fun cancelOrder(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderId = masterOrder?.id,
                status = ApiConstant.OrderStatus.CANCELED.value,
                isReturn = masterOrder?.firstOrder?.isReturn,
            )
        )
    }

    fun changeOrderStatus(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderId = masterOrder?.firstOrder?.id,
                status = ApiConstant.OrderStatus.getShopOwnerNextStatus(masterOrder?.firstOrder?.status)?.value,
                isReturn = masterOrder?.firstOrder?.isReturn,
            )
        )
    }

}