package com.g7.soft.pureDot.ui.screen.seeAll.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllReviewsViewModelFactory(
    private val itemId: Int?,
    private val tokenId: String,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AllReviewsViewModel::class.java)) {
            AllReviewsViewModel(
                itemId = itemId,
                tokenId = tokenId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}