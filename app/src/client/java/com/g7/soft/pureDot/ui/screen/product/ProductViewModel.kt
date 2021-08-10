package com.g7.soft.pureDot.ui.screen.product

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.ProductDetailsModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import java.util.*

class ProductViewModel(val product: ProductModel?) : ViewModel() {

    val productDetailsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val productDetailsResponse = MediatorLiveData<NetworkRequestResponse<ProductDetailsModel>>()

    private var sliderOffersTimer: Timer? = null
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

    val sizes
        get() = Transformations.map(productDetailsResponse) { response ->
            response.data?.sizes?.map { it.text }?.toList()
        }
    val flavours
        get() = Transformations.map(productDetailsResponse) { response ->
            response.data?.flavours?.map { it.name }?.toList()
        }
    val colors
        get() = Transformations.map(productDetailsResponse) { response ->
            response.data?.colors?.map { it.hexColor }?.toList()
        }

    /*val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val reviewsResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ReviewModel>>>>()*/


    fun fetchScreenData(langTag: String) {
        getItemDetails(langTag)
    }


    fun getItemDetails(langTag: String) {
        productDetailsResponse.value = NetworkRequestResponse.loading()
        sliderOffersTimer?.cancel() // release the auto slider timer

        // fetch request
        productDetailsResponse.apply {
            addSource(
                ProductRepository(langTag).getProductDetails(
                    productId = product?.id,
                )
            ) {
                productDetailsResponse.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer = Timer()
                    sliderOffersTimer?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition.value!! < (productDetailsResponse.value?.data?.images?.size
                                    ?: 0)
                            )
                                sliderOffersPosition.postValue(sliderOffersPosition.value!! + 1)
                            else
                                sliderOffersPosition.postValue(0)
                        }
                    }, 4000, 4000)
                }
            }
        }
    }

    fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            CartRepository(langTag).editCartQuantity(itemId = itemId,
            quantity = quantity,
            serviceDateTime = null))
    }
}