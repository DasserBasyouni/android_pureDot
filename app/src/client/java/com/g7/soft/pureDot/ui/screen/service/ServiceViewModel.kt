package com.g7.soft.pureDot.ui.screen.service

import androidx.lifecycle.*
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import com.g7.soft.pureDot.repo.ServiceRepository
import kotlinx.coroutines.Dispatchers
import java.util.*
import java.util.concurrent.TimeUnit

class ServiceViewModel(val service: ServiceModel?) : ViewModel() {

    val orderLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val orderResponse = MediatorLiveData<NetworkRequestResponse<MasterOrderModel>>()

    val selectedVariationsMap =
        MutableLiveData<HashMap<Int, ServiceVariationValueModel?>>().apply {
            this.value = hashMapOf()
        }
    val selectedVariations = Transformations.map(selectedVariationsMap) {
        it.values.filterNotNull().toList()
    }

    val servantsSelectedPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val hour = MutableLiveData<String?>()
    val minute = MutableLiveData<String?>()
    val isAm = MutableLiveData<Boolean?>().apply { this.value = true }
    val timeInSeconds: Long
        get() {
            return TimeUnit.HOURS.toSeconds((hour.value?.toIntOrNull() ?: 0).toLong())
                .plus(TimeUnit.MINUTES.toSeconds((minute.value?.toIntOrNull() ?: 0).toLong()))
                .plus(TimeUnit.HOURS.toSeconds((if (isAm.value == false) 12 else 0).toLong()))
        }

    val selectedBranchPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedBranch
        get() = serviceDetailsResponse.value?.data?.branches?.getOrNull(
            selectedBranchPosition.value?.minus(1) ?: -1
        )

    val quantityInCart = MediatorLiveData<Int>().apply { this.value = 1 }
    val reviewComment = MediatorLiveData<String>()
    val reviewRating = MediatorLiveData<Float>()

    val serviceDetailsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val serviceDetailsResponse = MediatorLiveData<NetworkRequestResponse<ServiceDetailsModel>>()

    private var sliderOffersTimer: Timer? = null
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

    val costResponse = MediatorLiveData<NetworkRequestResponse<Double>?>()
    val costLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    /*val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val reviewsResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ReviewModel>>>>()*/


    fun fetchData(langTag: String, tokenId: String?) {
        getItemDetails(langTag, tokenId = tokenId)
    }


    fun getItemDetails(langTag: String, tokenId: String?) {
        serviceDetailsResponse.value = NetworkRequestResponse.loading()
        sliderOffersTimer?.cancel() // release the auto slider timer

        // fetch request
        serviceDetailsResponse.apply {
            addSource(
                ServiceRepository(langTag).getServiceDetails(
                    tokenId = tokenId,
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

    fun addReview(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            ServiceRepository(langTag).addReview(
                tokenId = tokenId,
                productId = service?.id,
                rating = reviewRating.value?.toInt(),
                comment = reviewComment.value
            )
        )
    }


    fun getCost(langTag: String) {
        costResponse.value = NetworkRequestResponse.loading()

        // fetch request
        costResponse.apply {
            addSource(
                ServiceRepository(langTag).getCost(
                    serviceId = service?.id,
                    servants = servantsSelectedPosition.value,
                    variations = selectedVariations.value
                )
            ) {
                costResponse.value = it
            }
        }
    }

    fun checkCartItems(langTag: String, tokenId: String?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                OrderRepository(langTag).checkCartItems(
                    tokenId = tokenId,
                    serviceId = service?.id,
                    servantsCount = servantsSelectedPosition.value,
                    apiShopOrders = listOf(ApiShopOrderModel(
                        shopId = serviceDetailsResponse.value?.data?.shop?.id,
                        branchId = selectedBranch?.id,
                        items = mutableListOf<ApiShopOrderItemModel>().apply {
                            for (variation in selectedVariations.value ?: listOf())
                                this.add(
                                    ApiShopOrderItemModel(
                                        productId = variation.productId,
                                        sourceId = variation.sourceId,
                                        stockId = variation.stockId,
                                        categoryId = variation.categoryId,
                                        quantity = quantityInCart.value,
                                        variationsIds = listOf()
                                    )
                                )
                        }
                    ))
                )
            )
        }

}
