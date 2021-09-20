package com.g7.soft.pureDot.ui.screen.profileEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.model.SignUpFieldsModel

class ProfileEditViewModelFactory(
    private val userData: UserDataModel?,
    private val signUpFields: SignUpFieldsModel?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            ProfileEditViewModel(
                userData = userData,
                signUpFields = signUpFields
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}