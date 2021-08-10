package com.g7.soft.pureDot.ui.screen.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ServiceModel

class ServiceViewModelFactory(
    private val service: ServiceModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ServiceViewModel::class.java)) {
            ServiceViewModel(
                service = service,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}