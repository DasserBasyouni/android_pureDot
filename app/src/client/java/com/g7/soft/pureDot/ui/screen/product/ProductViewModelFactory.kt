package com.g7.soft.pureDot.ui.screen.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ProductModel

class ProductViewModelFactory(
    private val product: ProductModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            ProductViewModel(
                product = product,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}