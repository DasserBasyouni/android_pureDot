package com.g7.soft.pureDot.ui.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.model.ServiceModel

class CheckoutViewModelFactory(
    private val service: ServiceModel?,
    private val selectedVariations: IntArray?,
    private val servantsNumber: Int?,
    private val time: Long?,
    private val date: Long?,
    private val quantity: Int?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            CheckoutViewModel(
                service = service,
                selectedVariations = selectedVariations,
                servantsNumber = servantsNumber,
                time = time,
                date = date,
                quantity = quantity,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}