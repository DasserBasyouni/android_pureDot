package com.g7.soft.pureDot.ui.screen.productCheckout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ServiceModel
import com.g7.soft.pureDot.model.StoreProductsCartDetailsModel

class ProductCheckoutViewModelFactory(
    private val service: ServiceModel?,
    private val selectedVariations: IntArray?,
    private val servantsNumber: Int?,
    private val time: Long?,
    private val date: Long?,
    private val quantity: Int?,
    private val currency: String?,
    private val storesProductsCartDetails: MutableList<StoreProductsCartDetailsModel>?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductCheckoutViewModel::class.java)) {
            ProductCheckoutViewModel(
                service = service,
                selectedVariations = selectedVariations,
                servantsNumber = servantsNumber,
                time = time,
                date = date,
                quantity = quantity,
                currency = currency,
                storesProductsCartDetails = storesProductsCartDetails
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}