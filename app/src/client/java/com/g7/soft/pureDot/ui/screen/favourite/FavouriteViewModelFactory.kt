package com.g7.soft.pureDot.ui.screen.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavouriteViewModelFactory(
    private val tokenId: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            FavouritesViewModel(
                tokenId = tokenId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}