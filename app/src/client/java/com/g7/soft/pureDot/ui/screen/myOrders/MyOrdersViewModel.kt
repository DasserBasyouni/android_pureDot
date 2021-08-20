package com.g7.soft.pureDot.ui.screen.myOrders

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository

class MyOrdersViewModel : ViewModel() {

    val ordersResponse = MediatorLiveData<NetworkRequestResponse<List<MasterOrderModel>>>()
    val ordersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getMyOrders(langTag: String, tokenId: String) {
        ordersResponse.value = NetworkRequestResponse.loading()
        ordersResponse.apply {
            this.addSource(OrderRepository(langTag).getMyOrders(tokenId = tokenId)) {
                ordersResponse.value = it
            }
        }
    }

}