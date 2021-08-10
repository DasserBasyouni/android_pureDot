package com.g7.soft.pureDot.ui.screen.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilterViewModelFactory(
    private val currency: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            FilterViewModel(
                currency = currency,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}