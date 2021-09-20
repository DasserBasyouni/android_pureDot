package com.g7.soft.pureDot.ui.screen.submitComplain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.ComplaintOrderModel
import com.g7.soft.pureDot.model.IdNameModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ComplaintRepository
import com.g7.soft.pureDot.util.ValidationUtils
import kotlinx.coroutines.Dispatchers

class SubmitComplainViewModel : ViewModel() {

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String?>()

    val complaintOrdersResponse =
        MediatorLiveData<NetworkRequestResponse<List<ComplaintOrderModel>?>>()
    val categoriesResponse = MediatorLiveData<NetworkRequestResponse<List<IdNameModel>?>>()
    val ordersPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val categoriesPosition = MutableLiveData<Int?>().apply { this.value = 0 }

    val selectedRelatedOrder
        get() = complaintOrdersResponse.value?.data?.getOrNull(
            ordersPosition.value ?: -1
        )
    val selectedCategory
        get() = categoriesResponse.value?.data?.getOrNull(
            categoriesPosition.value ?: -1
        )

    fun fetchData(langTag: String, tokenId: String?) {
        getComplaintReasons(langTag)
        getComplaintOrder(langTag, tokenId)
    }

    fun getComplaintReasons(langTag: String) {
        categoriesResponse.value = NetworkRequestResponse.loading()
        categoriesResponse.apply {
            this.addSource(
                ComplaintRepository(langTag).getComplaintReasons()
            ) { categoriesResponse.value = it }
        }
    }

    fun getComplaintOrder(langTag: String, tokenId: String?) {
        complaintOrdersResponse.value = NetworkRequestResponse.loading()
        complaintOrdersResponse.apply {
            this.addSource(
                ComplaintRepository(langTag).getComplaintOrder(
                    tokenId = tokenId
                )
            ) { complaintOrdersResponse.value = it }
        }
    }

    fun submit(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .setComplaintTitle(title.value)
            .setPassword(description.value)
            .setSelectedRelatedOrder(selectedRelatedOrder)
            .setSelectedCategory(selectedCategory)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            ComplaintRepository(langTag).addComplain(
                tokenId = tokenId,
                title = title.value,
                description = description.value,
                orderId = selectedRelatedOrder?.id,
                reasonType = selectedCategory?.id,
            )
        )
    }
}