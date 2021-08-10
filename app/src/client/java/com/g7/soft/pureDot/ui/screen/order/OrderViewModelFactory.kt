package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.OrderModel

class OrderViewModelFactory(
    private val order: OrderModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            OrderViewModel(
                order = order,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}