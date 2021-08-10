package com.g7.soft.pureDot.ui.screen.transferMoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TransferMoneyViewModelFactory(
    private val tokenId: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TransferMoneyViewModel::class.java)) {
            TransferMoneyViewModel(
                tokenId = tokenId,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}