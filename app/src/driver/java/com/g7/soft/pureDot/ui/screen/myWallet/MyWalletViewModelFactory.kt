package com.g7.soft.pureDot.ui.screen.myWallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyWalletViewModelFactory(
    private val tokenId: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyWalletViewModel::class.java)) {
            MyWalletViewModel(
                tokenId = tokenId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}