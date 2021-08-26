package com.g7.soft.pureDot.ui.screen.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CategoriesRepository
import com.g7.soft.pureDot.repo.ProductRepository
import com.g7.soft.pureDot.repo.StoreRepository
import kotlinx.coroutines.Dispatchers
import java.util.*

class HomeViewModel : ViewModel() {

    val storesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val storesResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<StoreModel>>>>()

    private var sliderOffersTimer0: Timer? = null
    val sliderOffersResponse0 = MediatorLiveData<NetworkRequestResponse<List<SliderOfferModel>>>()
    val sliderOffersLcee0 = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val sliderOffersPosition0 = MediatorLiveData<Int>().apply { this.value = 0 }

    private var sliderOffersTimer1: Timer? = null
    val sliderOffersResponse1 = MediatorLiveData<NetworkRequestResponse<List<SliderOfferModel>>>()
    val sliderOffersLcee1 = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val sliderOffersPosition1 = MediatorLiveData<Int>().apply { this.value = 0 }

    val categoriesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val categoriesResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<CategoryModel>>>>()

    val latestOffersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val latestOffersResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ProductModel>>>>()

    private var sliderOffersTimer2: Timer? = null
    val sliderOffersResponse2 = MediatorLiveData<NetworkRequestResponse<List<SliderOfferModel>>>()
    val sliderOffersLcee2 = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val sliderOffersPosition2 = MediatorLiveData<Int>().apply { this.value = 0 }

    val latestProductsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val latestProductsResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ProductModel>>>>()

    val bestSellingLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val bestSellingResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ProductModel>>>>()


    fun fetchScreenData(langTag: String) {
        getStores(langTag)
        getOffersSlider0(langTag, null, null)
        getOffersSlider1(langTag, null, null)
        getCategories(langTag)
        getOffersSlider2(langTag, null, null)
        getLatestOffers(langTag)
        getLatestProducts(langTag)
        getBestSelling(langTag)
    }

    fun getStores(langTag: String) {
        storesResponse.value = NetworkRequestResponse.loading()

        // fetch request
        storesResponse.apply {
            addSource(
                StoreRepository(langTag).getAll(
                    pageNumber = 1,
                    itemsPerPage = ProjectConstant.ITEMS_PER_PAGE,
                    searchText = null,
                    categoryId = null,
                )
            ) { storesResponse.value = it }
        }
    }

    fun getOffersSlider0(langTag: String, categoryId: String?, shopId: String?) {
        sliderOffersResponse0.value = NetworkRequestResponse.loading()
        sliderOffersTimer0?.cancel() // release the auto slider timer

        // fetch request
        sliderOffersResponse0.apply {
            addSource(
                ProductRepository(langTag).getSliderOffers(
                    categoryId = categoryId,
                    shopId = shopId,
                    type = ApiConstant.SliderOfferType.HOME_MAIN_SLIDER.value,
                )
            ) {
                sliderOffersResponse0.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer0 = Timer()
                    sliderOffersTimer0?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition0.value!! < (sliderOffersResponse0.value?.data?.size
                                    ?: 0)
                            )
                                sliderOffersPosition0.postValue(sliderOffersPosition0.value!! + 1)
                            else
                                sliderOffersPosition0.postValue(0)
                        }
                    }, 4000, 4000)
                }
            }
        }
    }

    fun getOffersSlider1(langTag: String, categoryId: String?, shopId: String?) {
        sliderOffersResponse1.value = NetworkRequestResponse.loading()
        sliderOffersTimer1?.cancel() // release the auto slider timer

        // fetch request
        sliderOffersResponse1.apply {
            addSource(
                ProductRepository(langTag).getSliderOffers(
                    categoryId = categoryId,
                    shopId = shopId,
                    type = ApiConstant.SliderOfferType.HOME_CATEGORY_SLIDER.value,
                )
            ) {
                sliderOffersResponse1.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer1 = Timer()
                    sliderOffersTimer1?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition1.value!! < (sliderOffersResponse1.value?.data?.size
                                    ?: 0)
                            )
                                sliderOffersPosition1.postValue(sliderOffersPosition1.value!! + 1)
                            else
                                sliderOffersPosition1.postValue(0)
                        }
                    }, 4000, 4000)
                }
            }
        }
    }

    fun getOffersSlider2(langTag: String, categoryId: String?, shopId: String?) {
        sliderOffersResponse2.value = NetworkRequestResponse.loading()
        sliderOffersTimer2?.cancel() // release the auto slider timer

        // fetch request
        sliderOffersResponse2.apply {
            addSource(
                ProductRepository(langTag).getSliderOffers(
                    categoryId = categoryId,
                    shopId = shopId,
                    type = ApiConstant.SliderOfferType.HOME_LATEST_PRODUCT.value,
                )
            ) {
                sliderOffersResponse2.value = it

                // start auto slider
                if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                    sliderOffersTimer2 = Timer()
                    sliderOffersTimer2?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (sliderOffersPosition2.value!! < (sliderOffersResponse2.value?.data?.size
                                    ?: 0)
                            )
                                sliderOffersPosition2.postValue(sliderOffersPosition2.value!! + 1)
                            else
                                sliderOffersPosition2.postValue(0)
                        }
                    }, 4000, 4000)
                }
            }
        }
    }

    fun getCategories(langTag: String) {
        categoriesResponse.value = NetworkRequestResponse.loading()

        // fetch request
        categoriesResponse.apply {
            addSource(
                CategoriesRepository(langTag).getCategories(
                    pageNumber = 1,
                    itemsPerPage = ProjectConstant.ITEMS_PER_PAGE,
                    searchText = null,
                    shopId = null
                )
            ) { categoriesResponse.value = it }
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
                    shopId = null
                )
            ) { latestOffersResponse.value = it }
        }
    }

    /*fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(CartRepository(langTag).editCartQuantity(itemId = itemId,
            quantity = quantity,
            serviceDateTime = null))
    }*/

    fun getLatestProducts(langTag: String) {
        latestProductsResponse.value = NetworkRequestResponse.loading()

        // fetch request
        latestProductsResponse.apply {
            addSource(
                ProductRepository(langTag).getLatestProducts(
                    pageNumber = 1,
                    itemPerPage = 9,
                    shopId = null,
                )
            ) { latestProductsResponse.value = it }
        }
    }

    fun getBestSelling(langTag: String) {
        bestSellingResponse.value = NetworkRequestResponse.loading()

        // fetch request
        bestSellingResponse.apply {
            addSource(
                ProductRepository(langTag).getBestSelling(
                    pageNumber = 1,
                    itemPerPage = ProjectConstant.ITEMS_PER_PAGE,
                    shopId = null,
                )
            ) { bestSellingResponse.value = it }
        }
    }

    fun editWishList(langTag: String, tokenId: String?, productId: String?, doAdd: Boolean) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())
            emitSource(
                ProductRepository(langTag).editWishList(
                    tokenId = tokenId,
                    productId = productId,
                    doAdd = doAdd
                )
            )
        }
}