package com.g7.soft.pureDot.ui.screen.workingHours

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.WorkingTimesModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.AvailabilityRepository

class WorkingHoursViewModel : ViewModel() {

    val workingHoursResponse = MediatorLiveData<NetworkRequestResponse<WorkingTimesModel>>()
    val workingHoursLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getMyOrders(langTag: String, tokenId: String) {
        workingHoursResponse.value = NetworkRequestResponse.loading()
        workingHoursResponse.apply {
            this.addSource(AvailabilityRepository(langTag).getWorkingHours(tokenId = tokenId)) {
                workingHoursResponse.value = it
            }
        }
    }

}