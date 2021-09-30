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
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import java.util.*

class ProductViewModel(
    val product: ProductModel?,
    internal val productId: String?
) : ViewModel() {

    val selectedBranchPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedBranch
        get() = productDetailsResponse.value?.data?.branches?.getOrNull(
            selectedBranchPosition.value?.minus(1) ?: -1
        )

    val reviewComment = MediatorLiveData<String>()
    val reviewRating = MediatorLiveData<Float>()
    val quantityInCart = MediatorLiveData<Int>().apply { this.value = 1 }
    val productDetailsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val productDetailsResponse = MediatorLiveData<NetworkRequestResponse<ProductDetailsModel>>()

    val costResponse = MediatorLiveData<NetworkRequestResponse<Double>?>()
    val costLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val selectedVariationsIdsMap =
        MediatorLiveData<HashMap<Int, String?>>().apply { this.value = hashMapOf() }
    val selectedVariationsIds = Transformations.map(selectedVariationsIdsMap) {
        it.values.toList().filterNotNull()
    }

    private var sliderOffersTimer: Timer? = null
    val sliderOffersPosition = MediatorLiveData<Int>().apply { this.value = 0 }

    /*val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val reviewsResponse =
        MediatorLiveData<NetworkRequestResponse<DataWithCountModel<List<ReviewModel>>>>()*/


    fun fetchData(langTag: String, tokenId: String?) {
        getItemDetails(langTag, tokenId)
    }


    fun getItemDetails(langTag: String, tokenId: String?) {
        productDetailsResponse.value = NetworkRequestResponse.loading()
        sliderOffersTimer?.cancel() // release the auto slider timer

        // fetch request
        productDetailsResponse.apply {
            addSource(
                ProductRepository(langTag).getProductDetails(
                    tokenId = tokenId,
                    productId = product?.id ?: productId,
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
            lifecycleScope = viewModelScope,
            context = context,
            price = costResponse.value?.data,
            productId = product?.id,
            categoryId = product?.categoryId,
            storeId = product?.shop?.id,
            selectedBranchId = selectedBranch?.id,
            quantityInCart = quantityInCart.value,
            variationsIds = selectedVariationsIds.value,
            onComplete = onComplete
        )
    }

    fun getTotalProductsPriceInCart(
        langTag: String,
        context: Context,
        onComplete: (totalPrice: Double) -> Unit
    ) {
        CartRepository(langTag).getTotalProductsPriceInCart(
            viewModelScope,
            context,
            onComplete
        )
    }

    fun addReview(langTag: String, tokenId: String?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            // validate inputs
            ValidationUtils()
                .setComment(reviewComment.value)
                .setRating(reviewRating.value)
                .getError()?.let {
                    emit(NetworkRequestResponse.invalidInputData(validationError = it))
                    return@liveData
                }

            emitSource(
                ProductRepository(langTag).addReview(
                    tokenId = tokenId,
                    productId = product?.id,
                    rating = reviewRating.value?.toInt(),
                    comment = reviewComment.value
                )
            )
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

    fun getCost(langTag: String) {
        costResponse.value = NetworkRequestResponse.loading()

        // fetch request
        costResponse.apply {
            addSource(
                ProductRepository(langTag).getCost(
                    productId = product?.id,
                    variations = selectedVariationsIds.value
                )
            ) {
                costResponse.value = it
            }
        }
    }

    /*fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            CartRepository(langTag).editCartQuantity(itemId = itemId,
            quantity = quantity,
            serviceDateTime = null))
    }*/
}