package com.g7.soft.pureDot.ui.screen.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.project.LceeModel

class FavouritesViewModel : ViewModel() {

    val productsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var productsPagedList: LiveData<PagedList<ProductModel>>? = null

}