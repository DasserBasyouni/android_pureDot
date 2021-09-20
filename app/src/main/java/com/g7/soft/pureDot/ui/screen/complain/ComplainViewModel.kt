package com.g7.soft.pureDot.ui.screen.complain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.CommentModel
import com.g7.soft.pureDot.model.ComplainModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ComplaintRepository
import com.g7.soft.pureDot.util.ValidationUtils
import kotlinx.coroutines.Dispatchers

class ComplainViewModel(val complain: ComplainModel?) : ViewModel() {

    val message = MutableLiveData<String?>()
    val reviewsResponse = MediatorLiveData<NetworkRequestResponse<MutableList<CommentModel>>>()
    val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getComplainComments(langTag: String, tokenId: String?) {
        reviewsResponse.value = NetworkRequestResponse.loading()
        reviewsResponse.apply {
            this.addSource(
                ComplaintRepository(langTag).getComplainComments(
                    tokenId = tokenId,
                    complainId = complain?.id
                )
            ) {
                reviewsResponse.value = it
            }
        }
    }


    fun addComplaintReply(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .setComment(message.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            ComplaintRepository(langTag).addComplaintReply(
                tokenId = tokenId,
                message = message.value,
                complainId = complain?.id
            )
        )
    }

    fun rateComplainService(langTag: String, tokenId: String?, message: String?, ratting: Float?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())
            // validate inputs
            ValidationUtils()
                .setComment(message)
                .setRating(ratting)
                .getError()?.let {
                    emit(NetworkRequestResponse.invalidInputData(validationError = it))
                    return@liveData
                }

            emitSource(
                ComplaintRepository(langTag).rateComplainService(
                    tokenId = tokenId,
                    message = message,
                    complainId = complain?.id,
                    rating = ratting?.toInt()
                )
            )
        }

}