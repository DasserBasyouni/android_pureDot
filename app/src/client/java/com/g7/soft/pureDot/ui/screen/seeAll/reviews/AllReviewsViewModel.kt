package com.g7.soft.pureDot.ui.screen.seeAll.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.ReviewModel
import com.g7.soft.pureDot.model.project.LceeModel

class AllReviewsViewModel(
    internal val itemId: Int?,
    internal val tokenId: String
) : ViewModel() {

    val reviewsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var reviewsPagedList: LiveData<PagedList<ReviewModel>>? = null

}