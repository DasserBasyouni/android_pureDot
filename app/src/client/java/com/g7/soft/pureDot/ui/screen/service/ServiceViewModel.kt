package com.g7.soft.pureDot.ui.screen.service

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.ServiceDetailsModel
import com.g7.soft.pureDot.model.ServiceModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.ServiceRepository
import kotlinx.coroutines.Dispatchers
import java.util.*

class ServiceViewModel(val service: ServiceModel?) : ViewModel() {

    val servantsSelectedPosition = MutableLiveData<Int?>().apply { this.value = 0 }

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

    fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            CartRepository(langTag).editCartQuantity(itemId = itemId,
            quantity = quantity,
            serviceDateTime = null))
    }
}