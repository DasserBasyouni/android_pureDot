package com.g7.soft.pureDot.ui.screen.service

import android.content.Context
import androidx.lifecycle.*
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.ServiceDetailsModel
import com.g7.soft.pureDot.model.ServiceModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.ServiceRepository
import kotlinx.coroutines.Dispatchers
import java.util.*
import java.util.concurrent.TimeUnit

class ServiceViewModel(val service: ServiceModel?) : ViewModel() {

    val selectedVariations =
        MutableLiveData<MutableList<Int>>().apply { this.value = mutableListOf() }
    val servantsSelectedPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val hour = MutableLiveData<String?>()
    val minute = MutableLiveData<String?>()
    val isAm = MutableLiveData<Boolean?>().apply { this.value = true }
    val timeInSeconds: Long
        get() {
            return TimeUnit.HOURS.toSeconds(( hour.value?.toIntOrNull() ?: 0).toLong())
                .plus(TimeUnit.MINUTES.toSeconds((minute.value?.toIntOrNull() ?: 0).toLong()))
                .plus(TimeUnit.HOURS.toSeconds((if (isAm.value == false) 12 else 0).toLong()))
        }

    val quantityInCart = MediatorLiveData<Int>().apply { this.value = 1 }
    val reviewComment = MediatorLiveData<String>()
    val reviewRating = MediatorLiveData<Float>()

    val serviceDetailsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val serviceDetailsResponse = MediatorLiveData<NetworkRequestResponse<ServiceDetailsModel>>()

    private var sliderOffersTimer: Timer? = null
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }


    /*val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val reviewsResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ReviewModel>>>>()*/


    fun fetchScreenData(langTag: String) {
        getItemDetails(langTag)
    }


    fun getItemDetails(langTag: String) {
        serviceDetailsResponse.value = NetworkRequestResponse.loading()
        sliderOffersTimer?.cancel() // release the auto slider timer

        // fetch request
        serviceDetailsResponse.apply {
            addSource(
                ServiceRepository(langTag).getServiceDetails(
                    serviceId = service?.id,
                )
            ) {
                serviceDetailsResponse.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer = Timer()
                    sliderOffersTimer?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition.value!! < (serviceDetailsResponse.value?.data?.images?.size
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

    /*fun addToCart(langTag: String, context: Context, onComplete: () -> Unit) {
        CartRepository(langTag).addServiceToCart(
            viewModelScope,
            context,
            service?.id,
            quantityInCart.value,
            onComplete
        )
    }*/

    fun addReview(langTag: String, tokenId: String?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                ServiceRepository(langTag).addReview(
                    tokenId = tokenId,
                    productId = service?.id,
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