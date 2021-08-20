package com.g7.soft.pureDot.ui.screen.returnFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.ProductModel

class ReturnViewModelFactory(
    private val masterOrder: MasterOrderModel?,
    private val selectedProduct: ProductModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RefundViewModel::class.java)) {
            RefundViewModel(
                masterOrder = masterOrder,
                selectedProduct = selectedProduct
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}