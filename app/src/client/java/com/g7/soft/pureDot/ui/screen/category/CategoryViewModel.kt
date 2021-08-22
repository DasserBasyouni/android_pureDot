package com.g7.soft.pureDot.ui.screen.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.SliderOfferModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import java.util.*

class CategoryViewModel(val category: CategoryModel?) : ViewModel() {

    /*val storesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val storesResponse = MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<StoreModel>>>>()*/

    private var sliderOffersTimer: Timer? = null
    val sliderOffersResponse = MediatorLiveData<NetworkRequestResponse<List<SliderOfferModel>>>()
    val sliderOffersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

    val productsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var productsPagedList: LiveData<PagedList<ProductModel>>? = null


    fun fetchScreenData(langTag: String) {
        //getStores(langTag)
        getOffersSlider(langTag, category?.id, null)
    }

    /*fun getStores(langTag: String) {
        storesResponse.value = NetworkRequestResponse.loading()

        // fetch request
        storesResponse.apply {
            addSource(
                StoreRepository(langTag).getAll(
                    pageNumber = 1,
                    itemsPerPage = ProjectConstant.ITEMS_PER_PAGE,
                    searchText = null,
                    categoryId = category?.id,
                )
            ) { storesResponse.value = it }
        }
    }*/

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


    fun editWishList(langTag: String, tokenId: String?, productId: Int?, doAdd: Boolean) =
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