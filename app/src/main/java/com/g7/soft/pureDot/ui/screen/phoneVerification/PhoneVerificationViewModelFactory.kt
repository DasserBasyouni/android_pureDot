package com.g7.soft.pureDot.ui.screen.phoneVerification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhoneVerificationViewModelFactory(
    private val isPasswordReset: Boolean,
    private val emailOrPhoneNumber: String?,
    private val isWalletVerification: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PhoneVerificationViewModel::class.java)) {
            PhoneVerificationViewModel(
                isPasswordReset = isPasswordReset,
                emailOrPhoneNumber = emailOrPhoneNumber,
                isWalletVerification = isWalletVerification
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}