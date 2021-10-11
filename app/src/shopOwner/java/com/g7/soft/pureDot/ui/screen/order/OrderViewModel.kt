package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ApiConstant.OrderStatus.Companion.getShopOwnerNextStatus
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class OrderViewModel(val masterOrder: MasterOrderModel?) : ViewModel() {

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
                    id = masterOrder?.id,
                )
            ) { orderResponse.value = it }
        }
    }

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

    fun changeOrderStatus(
        langTag: String,
        tokenId: String?,
        packageLength: String? = null,
        packageWidth: String? = null,
        packageHeight: String? = null,
        packageWeight: String? = null,
        packageDescription: String? = null
    ) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderId = masterOrder?.firstOrder?.id,
                status = getShopOwnerNextStatus(masterOrder?.firstOrder?.status)?.value,
                isReturn = masterOrder?.firstOrder?.isReturn,
                packageLength = packageLength,
                packageWidth = packageWidth,
                packageHeight = packageHeight,
                packageWeight = packageWeight,
                packageDescription = packageDescription
            )
        )
    }

}