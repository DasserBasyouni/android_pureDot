package com.g7.soft.pureDot.ui.screen.workingHours

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.WorkingDayModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.AvailabilityRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class WorkingHoursViewModel : ViewModel() {

    val expandedIndex = MutableLiveData<Int?>()
    val workingHoursResponse = MediatorLiveData<NetworkRequestResponse<List<WorkingDayModel>>>()
    val workingHoursLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getMyOrders(langTag: String, tokenId: String?) {
        workingHoursResponse.value = NetworkRequestResponse.loading()
        workingHoursResponse.apply {
            this.addSource(AvailabilityRepository(langTag).getWorkingHours(tokenId = tokenId)) {
                workingHoursResponse.value = it
            }
        }
    }

    fun submitWorkingDays(langTag: String, tokenId:String?, workingDays: List<WorkingDayModel>?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .workingDays(workingDays)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            AvailabilityRepository(langTag).addEditWorkingHours(
                tokenId = tokenId,
                workingDays = workingDays,
            )
        )
    }

}