package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository

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

}