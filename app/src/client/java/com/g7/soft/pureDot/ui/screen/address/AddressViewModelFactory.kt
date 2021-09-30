package com.g7.soft.pureDot.ui.screen.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.UserDataModel

class AddressViewModelFactory(private val userData: UserDataModel?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            AddressViewModel(
                userData = userData,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}