package com.g7.soft.pureDot.ui.screen.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.StoreModel

class StoreViewModelFactory(
    private val store: StoreModel?,
    private val shopId: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            StoreViewModel(
                store = store,
                shopId = shopId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}