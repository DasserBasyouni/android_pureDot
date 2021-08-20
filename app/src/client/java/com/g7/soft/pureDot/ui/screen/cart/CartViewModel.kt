package com.g7.soft.pureDot.ui.screen.cart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.CartItemsModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository

class CartViewModel : ViewModel() {

    val cartItemsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val cartItemsResponse = MediatorLiveData<NetworkRequestResponse<CartItemsModel>>()

    val totalPrice
        get() = Transformations.map(cartItemsResponse) {
            it.data?.products?.sumOf { product ->
                product.quantityInCart!! * (product.priceWithDiscount ?: 0.00)
            }?.plus(it.data.products.sumOf { product ->
                product.quantityInCart!! * (product.vat ?: 0.00)
            })
        }
    val currency
        get() = Transformations.map(cartItemsResponse) {
            it.data?.products?.first()?.currency
        }

    fun getCartItems(langTag: String, tokenId: String) {
        cartItemsResponse.value = NetworkRequestResponse.loading()

        // fetch request
        cartItemsResponse.apply {
            addSource(
                CartRepository(langTag).getCartItems(
                    tokenId = tokenId
                )
            ) { cartItemsResponse.value = it }
        }
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
}