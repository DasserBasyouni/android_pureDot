package com.g7.soft.pureDot.ui.screen.complain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.CommentModel
import com.g7.soft.pureDot.model.ComplainModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ComplainRepository

class ComplainViewModel(val complain: ComplainModel?) : ViewModel() {

    val message = MutableLiveData<String?>()
    val reviewsResponse = MediatorLiveData<NetworkRequestResponse<List<CommentModel>>>()
    val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getComplainComments(langTag: String, tokenId: String) {
        reviewsResponse.value = NetworkRequestResponse.loading()
        reviewsResponse.apply {
            this.addSource(
                ComplainRepository(langTag).getComplainComments(
                    tokenId = tokenId,
                    complainId = complain?.id
                )
            ) {
                reviewsResponse.value = it
            }
        }
    }

}