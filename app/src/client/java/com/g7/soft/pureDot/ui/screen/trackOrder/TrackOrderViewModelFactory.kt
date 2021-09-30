package com.g7.soft.pureDot.ui.screen.trackOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackOrderViewModelFactory(
    private val orderId: String?,
    private val orderNumber: Int?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrackOrderViewModel::class.java)) {
            TrackOrderViewModel(
                orderId = orderId,
                orderNumber = orderNumber,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}