package com.g7.soft.pureDot.ui.screen.complain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ComplainModel

class ComplainViewModelFactory(
    private val complain: ComplainModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ComplainViewModel::class.java)) {
            ComplainViewModel(
                complain = complain,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}