package com.g7.soft.pureDot.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.CategoryModel

class CategoryViewModelFactory(
    private val category: CategoryModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            CategoryViewModel(
                category = category,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}