package com.g7.soft.pureDot.ui.screen.myOrders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel

class MyOrdersViewModel(internal val tokenId: String?) : ViewModel() {

    val ordersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var ordersPagedList: LiveData<PagedList<MasterOrderModel>>? = null

}