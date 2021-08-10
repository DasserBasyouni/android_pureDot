package com.g7.soft.pureDot.ui.screen.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.ServiceModel
import com.g7.soft.pureDot.model.project.LceeModel

class ServicesViewModel : ViewModel() {

    val servicesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var servicesPagedList: LiveData<PagedList<ServiceModel>>? = null

}