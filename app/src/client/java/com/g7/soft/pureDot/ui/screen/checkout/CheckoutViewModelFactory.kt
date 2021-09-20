package com.g7.soft.pureDot.ui.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ApiShopOrderModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.ServiceVariationValueModel

class CheckoutViewModelFactory(
    private val masterOrder: MasterOrderModel?,
    private val productApiShopOrder: List<ApiShopOrderModel>?,
    private val serviceId: String?,
    private val serviceBranchId: String?,
    private val serviceShopId: String?,
    private val serviceVariations: List<ServiceVariationValueModel>?,
    private val serviceQuantity: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            CheckoutViewModel(
                masterOrder = masterOrder,
                productApiShopOrder = productApiShopOrder,
                serviceId = serviceId,
                serviceBranchId = serviceBranchId,
                serviceShopId = serviceShopId,
                serviceVariations = serviceVariations,
                serviceQuantity = serviceQuantity
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}