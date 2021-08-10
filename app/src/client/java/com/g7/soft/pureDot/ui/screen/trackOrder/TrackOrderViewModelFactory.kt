package com.g7.soft.pureDot.ui.screen.trackOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.OrderModel

class TrackOrderViewModelFactory(
    private val order: OrderModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrackOrderViewModel::class.java)) {
            TrackOrderViewModel(
                order = order,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}