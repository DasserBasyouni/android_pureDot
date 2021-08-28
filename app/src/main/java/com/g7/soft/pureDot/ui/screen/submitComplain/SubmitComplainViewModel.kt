package com.g7.soft.pureDot.ui.screen.submitComplain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.ComplaintOrderModel
import com.g7.soft.pureDot.model.DataWithCountModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CategoriesRepository
import com.g7.soft.pureDot.repo.ComplainRepository
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class SubmitComplainViewModel : ViewModel() {

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String?>()

    val complaintOrdersResponse = MediatorLiveData<NetworkRequestResponse<List<ComplaintOrderModel>?>>()
    val categoriesResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<CategoryModel>?>>>()
    val ordersPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val categoriesPosition = MutableLiveData<Int?>().apply { this.value = 0 }


    fun fetchData(langTag: String, tokenId: String) {
        getCategories(langTag)
        getComplaintOrder(langTag, tokenId)
    }

    fun getCategories(langTag: String) {
        categoriesResponse.value = NetworkRequestResponse.loading()
        categoriesResponse.apply {
            this.addSource(
                CategoriesRepository(langTag).getCategories(
                    pageNumber = null, // todo api note null = get all
                    itemsPerPage = null,
                    searchText = null,
                    shopId = null
                )
            ) { categoriesResponse.value = it }
        }
    }

    fun getComplaintOrder(langTag: String, tokenId: String) {
        complaintOrdersResponse.value = NetworkRequestResponse.loading()
        complaintOrdersResponse.apply {
            this.addSource(
                OrderRepository(langTag).getComplaintOrder(
                    tokenId = tokenId
                )
            ) { complaintOrdersResponse.value = it }
        }
    }

    fun submit(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            ComplainRepository(langTag).addComplain(
                tokenId = tokenId,
                title = title.value,
                description = description.value,
                relatedOrderNumber = complaintOrdersResponse.value?.data?.get(ordersPosition.value!!)?.number,
                categoryId = categoriesResponse.value?.data?.data?.get(categoriesPosition.value!!)?.id,
            )
        )
    }
}