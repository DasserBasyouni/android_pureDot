package com.g7.soft.pureDot.ui.screen.cart

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g7.soft.pureDot.model.ApiShopOrderModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.OrderRepository

class CartViewModel : ViewModel() {

    val orderLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    val orderResponse = MediatorLiveData<NetworkRequestResponse<MasterOrderModel>>()
    val apiShopOrders = MediatorLiveData<List<ApiShopOrderModel>>()


    fun checkCartItems(langTag: String, tokenId: String?, context: Context) {
        orderResponse.value = NetworkRequestResponse.loading()

        CartRepository(langTag).getProductsInCart(
            viewModelScope,
            context,
            onComplete = { productCart ->
                apiShopOrders.value = productCart?.mapNotNull { apiShopOrder -> apiShopOrder?.apiShopOrder }

                // fetch request
                if (productCart.isNullOrEmpty())
                    orderResponse.value = NetworkRequestResponse.success()
                else
                    orderResponse.apply {
                        addSource(
                            OrderRepository(langTag).checkCartItems(
                                tokenId = tokenId,
                                apiShopOrders = apiShopOrders.value
                            )
                        ) { orderResponse.value = it }
                    }
            }
        )
    }
}