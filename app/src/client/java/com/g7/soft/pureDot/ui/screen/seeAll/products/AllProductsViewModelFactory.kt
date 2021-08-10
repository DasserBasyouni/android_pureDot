package com.g7.soft.pureDot.ui.screen.seeAll.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.constant.ApiConstant

class AllProductsViewModelFactory(
    private val sliderType: ApiConstant.SliderOfferType,
    private val storeId: Int?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AllProductsViewModel::class.java)) {
            AllProductsViewModel(
                sliderType = sliderType,
                storeId = storeId
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}