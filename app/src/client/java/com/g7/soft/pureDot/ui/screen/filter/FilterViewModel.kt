package com.g7.soft.pureDot.ui.screen.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.model.project.LceeModel

class FilterViewModel : ViewModel() {

    val searchText = MediatorLiveData<String>()
    var selectedCategoriesIds = mutableListOf<String>()
    var selectedStoresIds = mutableListOf<String>()
    val minStars: MutableList<Int>
        get() {
            val list = mutableListOf<Int>()

            if (doOneStar.value == true) list.add(1)
            if (doTwoStar.value == true) list.add(2)
            if (doThreeStar.value == true) list.add(3)
            if (doFourStar.value == true) list.add(4)
            if (doFiveStar.value == true) list.add(5)

            return list
        }

    val categoriesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var categoriesPagedList: LiveData<PagedList<CategoryModel>>? = null

    val storesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var storesPagedList: LiveData<PagedList<StoreModel>>? = null

    val doOneStar = MediatorLiveData<Boolean>().apply { this.value = false }
    val doTwoStar = MediatorLiveData<Boolean>().apply { this.value = false }
    val doThreeStar = MediatorLiveData<Boolean>().apply { this.value = false }
    val doFourStar = MediatorLiveData<Boolean>().apply { this.value = false }
    val doFiveStar = MediatorLiveData<Boolean>().apply { this.value = false }

    val minPrice = MediatorLiveData<String>().apply { this.value = "0" }
    val maxPrice = MediatorLiveData<String>().apply { this.value = "9999" }

    fun resetFilter() {
        doOneStar.value = false
        doTwoStar.value = false
        doThreeStar.value = false
        doFourStar.value = false
        doFiveStar.value = false

        minPrice.value = "0"
        maxPrice.value = "9999"
    }
}