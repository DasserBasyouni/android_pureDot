package com.g7.soft.pureDot.ui.screen.seeAll.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.project.LceeModel

class AllCategoriesViewModel : ViewModel() {


    val categoriesLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var categoriesPagedList: LiveData<PagedList<CategoryModel>>? = null


}