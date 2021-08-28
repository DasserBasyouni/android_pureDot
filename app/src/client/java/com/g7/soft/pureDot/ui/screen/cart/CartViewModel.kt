package com.g7.soft.pureDot.ui.screen.cart

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g7.soft.pureDot.model.StoreProductsCartDetailsModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository

class CartViewModel : ViewModel() {

    val productsInCartLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val productsInCartResponse =
        MediatorLiveData<NetworkRequestResponse<MutableList<StoreProductsCartDetailsModel>>>()

    val totalPrice
        get() = Transformations.map(productsInCartResponse) {
            it.data?.sumOf { dataModel ->
                dataModel.totalCost ?: 0.0
            }
        }
    val currency
        get() = Transformations.map(productsInCartResponse) {
            it.data?.first()?.currency
        }

    /*fun editCartQuantity(langTag: String, itemId: Int?, quantity: Int) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            CartRepository(langTag).editCartQuantity(
                itemId = itemId,
                quantity = quantity,
                serviceDateTime = null
            )
        )
    }*/

    fun getProductsInCart(langTag: String, tokenId: String, context: Context) {
        productsInCartResponse.value = NetworkRequestResponse.loading()

        CartRepository(langTag).getProductsInCart(
            viewModelScope,
            context,
            onComplete = {
                // fetch request
                productsInCartResponse.apply {
                    addSource(
                        CartRepository(langTag).checkCartProducts(
                            tokenId = tokenId,
                            ids = it?.mapNotNull { it?.apiId },
                            quantities = it?.mapNotNull { it?.quantityInCart },
                        )
                    ) { productsInCartResponse.value = it }
                }
            }
        )
    }
}