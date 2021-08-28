package com.g7.soft.pureDot.ui.screen.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.model.project.LceeModel

class FilterViewModel : ViewModel() {

    var currency: String? = null

    val searchText = MediatorLiveData<String>()
    var selectedCategoriesIds = mutableListOf<String>()
    var selectedStoresIds = mutableListOf<String>()
    val minStars: MutableList<Int> get() {
        val list = mutableListOf<Int>()

        when{
            doOneStar.value == true -> list.add(1)
            doTwoStar.value == true -> list.add(2)
            doThreeStar.value == true -> list.add(3)
            doFourStar.value == true -> list.add(4)
            doFiveStar.value == true -> list.add(5)
        }

        return list
    }

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