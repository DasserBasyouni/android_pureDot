package com.g7.soft.pureDot.ui.screen.trackOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.OrderModel

class TrackOrderViewModelFactory(
    private val order: OrderModel?,
    private val masterOrderNumber: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrackOrderViewModel::class.java)) {
            TrackOrderViewModel(
                order = order,
                masterOrderNumber = masterOrderNumber
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}