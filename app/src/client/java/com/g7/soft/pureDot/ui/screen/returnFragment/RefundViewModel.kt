package com.g7.soft.pureDot.ui.screen.returnFragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.ShippingCostModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class RefundViewModel(val masterOrder: MasterOrderModel?, val selectedProduct: ProductModel?) :
    ViewModel() {

    val selectedShippingMethodViewId = MediatorLiveData<Int>().apply { this.value = R.id.deliveryAppRb }
    val shippingCostResponse = MediatorLiveData<NetworkRequestResponse<ShippingCostModel>>()
    val shippingCostLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun calculateRefundShipping(langTag: String) {
        shippingCostResponse.value = NetworkRequestResponse.loading()
        shippingCostResponse.apply {
            this.addSource(
                OrderRepository(langTag).calculateRefundShipping(
                    orderNumber = masterOrder?.number,
                    productId = selectedProduct?.id,
                    shippingMethod = selectedShippingMethodViewId.value // todo
                )
            ) {
                shippingCostResponse.value = it
            }
        }
    }

    fun refund(langTag: String, ) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).refund(
                orderNumber = masterOrder?.number,
                productId = selectedProduct?.id,
                shippingMethod = selectedShippingMethodViewId.value // todo
            )
        )
    }
}