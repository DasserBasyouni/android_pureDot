package com.g7.soft.pureDot.ui.screen.submitComplain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.DataWithCountModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CategoriesRepository
import com.g7.soft.pureDot.repo.ComplainRepository
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class SubmitComplainViewModel : ViewModel() {

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String?>()

    val ordersResponse = MediatorLiveData<NetworkRequestResponse<List<OrderModel>?>>()
    val categoriesResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<CategoryModel>?>>>()
    val ordersPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val categoriesPosition = MutableLiveData<Int?>().apply { this.value = 0 }


    fun fetchData(langTag: String, tokenId: String) {
        getCategories(langTag)
        getOrders(langTag, tokenId)
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

    fun getOrders(langTag: String, tokenId: String) {
        ordersResponse.value = NetworkRequestResponse.loading()
        ordersResponse.apply {
            this.addSource(
                OrderRepository(langTag).getMyOrders(
                    tokenId = tokenId
                )
            ) { ordersResponse.value = it }
        }
    }

    fun submit(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            ComplainRepository(langTag).addComplain(
                tokenId = tokenId,
                title = title.value,
                description = description.value,
                relatedOrderNumber = ordersResponse.value?.data?.get(ordersPosition.value!!)?.number,
                categoryId = categoriesResponse.value?.data?.data?.get(categoriesPosition.value!!)?.id,
            )
        )
    }
}