package com.g7.soft.pureDot.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.MasterOrderModel

class OrderViewModelFactory(
    private val masterOrder: MasterOrderModel?,
    private val masterOrderId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            OrderViewModel(
                masterOrder = masterOrder,
                masterOrderId = masterOrderId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}