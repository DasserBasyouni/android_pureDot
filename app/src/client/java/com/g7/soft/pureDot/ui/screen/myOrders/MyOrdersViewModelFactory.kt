package com.g7.soft.pureDot.ui.screen.myOrders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel

class MyOrdersViewModelFactory(private val tokenId: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyOrdersViewModel::class.java)) {
            MyOrdersViewModel(
                tokenId = tokenId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}