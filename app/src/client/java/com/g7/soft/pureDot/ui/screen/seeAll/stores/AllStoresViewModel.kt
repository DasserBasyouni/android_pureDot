package com.g7.soft.pureDot.ui.screen.seeAll.stores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.model.project.LceeModel

class AllStoresViewModel : ViewModel() {

    val storesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var storesPagedList: LiveData<PagedList<StoreModel>>? = null

}