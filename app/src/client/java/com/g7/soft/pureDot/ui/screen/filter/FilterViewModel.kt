package com.g7.soft.pureDot.ui.screen.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.model.project.LceeModel

class FilterViewModel(val currency: String?) : ViewModel() {

    val categoriesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var categoriesPagedList: LiveData<PagedList<CategoryModel>>? = null

    val storesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var storesPagedList: LiveData<PagedList<StoreModel>>? = null

    val doOneStar = MediatorLiveData<Boolean>().apply { this.value = true }
    val doTwoStar = MediatorLiveData<Boolean>().apply { this.value = true }
    val doThreeStar = MediatorLiveData<Boolean>().apply { this.value = true }
    val doFourStar = MediatorLiveData<Boolean>().apply { this.value = true }
    val doFiveStar = MediatorLiveData<Boolean>().apply { this.value = true }

    val minPrice = MediatorLiveData<String>().apply { this.value = "0" }
    val maxPrice = MediatorLiveData<String>().apply { this.value = "9999" }

    fun resetFilter(){
        doOneStar.value = true
        doTwoStar.value = true
        doThreeStar.value = true
        doFourStar.value = true
        doFiveStar.value = true

        minPrice.value = "0"
        maxPrice.value = "9999"
    }
}