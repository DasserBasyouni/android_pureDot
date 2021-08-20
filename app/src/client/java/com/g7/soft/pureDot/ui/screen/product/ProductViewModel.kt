package com.g7.soft.pureDot.ui.screen.product

import android.content.Context
import androidx.lifecycle.*
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

    val selectedBranchPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val reviewComment = MediatorLiveData<String>()
    val reviewRating = MediatorLiveData<Float>()
    val quantityInCart = MediatorLiveData<Int>().apply { this.value = 1 }
    val productDetailsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val productDetailsResponse = MediatorLiveData<NetworkRequestResponse<ProductDetailsModel>>()

    private var sliderOffersTimer: Timer? = null
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

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

    fun addProductToCart(langTag: String, context: Context, onComplete: () -> Unit) {
        CartRepository(langTag).addProductToCart(
            viewModelScope,
            context,
            product?.id,
            quantityInCart.value,
            onComplete
        )
    }

    fun addReview(langTag: String, tokenId: String?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                ProductRepository(langTag).addReview(
                    tokenId = tokenId,
                    productId = product?.id,
                    rating = reviewRating.value,
                    comment = reviewComment.value
                )
            )
        }

    /*fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            CartRepository(langTag).editCartQuantity(itemId = itemId,
            quantity = quantity,
            serviceDateTime = null))
    }*/
}