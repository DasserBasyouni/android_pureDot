package com.g7.soft.pureDot.ui.screen.returnFragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.ShippingCostModel
import com.g7.soft.pureDot.model.ShippingMethodModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class RefundViewModel(val masterOrder: MasterOrderModel?, val selectedProduct: ProductModel?) :
    ViewModel() {

    val shippingMethodsResponse =
        MediatorLiveData<NetworkRequestResponse<List<ShippingMethodModel>?>?>()
    val shippingMethodsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel(allowEmpty = true) }

    val selectedShippingMethodId = MutableLiveData<String?>()
    val haveSelectedShippingMethod = MutableLiveData<Boolean?>()

    val shippingCostResponse = MediatorLiveData<NetworkRequestResponse<ShippingCostModel>>()
    val shippingCostLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun calculateRefundShipping(langTag: String) {
        shippingCostResponse.value = NetworkRequestResponse.loading()
        shippingCostResponse.apply {
            this.addSource(
                OrderRepository(langTag).calculateRefundShipping(
                    detailsId = selectedProduct?.detailsId,
                    shippingMethod = selectedShippingMethodId.value
                )
            ) {
                shippingCostResponse.value = it
            }
        }
    }

    fun returnItem(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            OrderRepository(langTag).returnItem(
                detailsId = selectedProduct?.detailsId,
                itemTotalPrice = shippingCostResponse.value?.data?.itemTotalPrice,
                refundAmount = shippingCostResponse.value?.data?.refundAmount,
                shippingCost = shippingCostResponse.value?.data?.shippingCost,
                commission = shippingCostResponse.value?.data?.commission,
                vat = shippingCostResponse.value?.data?.vat,
                driverEarning = shippingCostResponse.value?.data?.driverEarning,
                driverVat = shippingCostResponse.value?.data?.driverVat,
                deliveryCommissionVat = shippingCostResponse.value?.data?.deliveryCommissionVat,
                shippingMethod = selectedShippingMethodId.value
            )
        )
    }

    fun getShippingMethods(langTag: String) {
        shippingMethodsResponse.value = NetworkRequestResponse.loading()
        shippingMethodsResponse.apply {
            this.addSource(
                OrderRepository(langTag).getShippingMethods(
                    addressId = masterOrder?.clientAddress?.id,
                    branchesIds = listOfNotNull(masterOrder?.orders?.firstOrNull {
                        it.products?.contains(selectedProduct) == true
                    }?.branch?.id)
                )
            ) {
                shippingMethodsResponse.value = it
            }
        }
    }
}