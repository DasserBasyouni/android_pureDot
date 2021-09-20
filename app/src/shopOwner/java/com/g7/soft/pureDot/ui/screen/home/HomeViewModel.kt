package com.g7.soft.pureDot.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel

class HomeViewModel(internal val tokenId: String?) : ViewModel() {

    val areNewOrdersSelected = MediatorLiveData<Boolean>().apply { this.value = true }

    val newOrdersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var newOrdersPagedList: LiveData<PagedList<MasterOrderModel>>? = null

    val pendingOrdersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var pendingOrdersPagedList: LiveData<PagedList<MasterOrderModel>>? = null
}