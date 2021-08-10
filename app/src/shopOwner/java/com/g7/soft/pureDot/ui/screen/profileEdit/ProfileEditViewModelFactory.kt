package com.g7.soft.pureDot.ui.screen.profileEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ClientDataModel

class ProfileEditViewModelFactory(
    private val userData: ClientDataModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            ProfileEditViewModel(
                userData = userData,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}