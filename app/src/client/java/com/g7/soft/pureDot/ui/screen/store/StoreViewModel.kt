package com.g7.soft.pureDot.ui.screen.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ProductRepository
import java.util.*

class StoreViewModel(val store: StoreModel?) : ViewModel() {

    val categoriesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var categoriesPagedList: LiveData<PagedList<CategoryModel>>? = null

    private var sliderOffersTimer: Timer? = null
    val sliderOffersResponse = MediatorLiveData<NetworkRequestResponse<List<SliderOfferModel>>>()
    val sliderOffersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

    val latestOffersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val latestOffersResponse = MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ProductModel>>>>()

    val latestProductsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val latestProductsResponse = MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ProductModel>>>>()


    fun fetchScreenData(langTag: String) {
        getOffersSlider(langTag, null, store?.id)
        getLatestOffers(langTag)
        getLatestProducts(langTag)
    }

    fun getOffersSlider(langTag: String, categoryId: Int?, shopId: Int?) {
        sliderOffersResponse.value = NetworkRequestResponse.loading()
        sliderOffersTimer?.cancel() // release the auto slider timer

        // fetch request
        sliderOffersResponse.apply {
            addSource(
                ProductRepository(langTag).getSliderOffers(
                    categoryId = categoryId,
                    shopId = shopId,
                    type = ApiConstant.SliderOfferType.HOME_LATEST_PRODUCT.value,
                )
            ) {
                sliderOffersResponse.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer = Timer()
                    sliderOffersTimer?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition.value!! < (sliderOffersResponse.value?.data?.size
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

    fun getLatestOffers(langTag: String) {
        latestOffersResponse.value = NetworkRequestResponse.loading()

        // fetch request
        latestOffersResponse.apply {
            addSource(
                ProductRepository(langTag).getLatestOffers(
                    pageNumber = 1,
                    itemPerPage = 4,
                    searchText = null,
                    shopId = store?.id
                )
            ) { latestOffersResponse.value = it }
        }
    }

    fun getLatestProducts(langTag: String) {
        latestProductsResponse.value = NetworkRequestResponse.loading()

        // fetch request
        latestProductsResponse.apply {
            addSource(
                ProductRepository(langTag).getLatestProducts(
                    pageNumber = 1,
                    itemPerPage = 9,
                    searchText = null,
                    shopId = store?.id,
                )
            ) { latestProductsResponse.value = it }
        }
    }
}