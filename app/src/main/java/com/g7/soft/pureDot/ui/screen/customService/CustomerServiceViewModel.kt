package com.g7.soft.pureDot.ui.screen.customService

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.ComplainModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ComplaintRepository

class CustomerServiceViewModel : ViewModel() {

    val complainResponse = MediatorLiveData<NetworkRequestResponse<List<ComplainModel>>>()
    val complainLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getComplains(langTag: String, tokenId: String?) {
        complainResponse.value = NetworkRequestResponse.loading()
        complainResponse.apply {
            this.addSource(ComplaintRepository(langTag).getComplains(tokenId = tokenId)) {
                complainResponse.value = it
            }
        }
    }

}