package com.g7.soft.pureDot.ui.screen.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.ProductRepository
import kotlinx.coroutines.Dispatchers

class FavouritesViewModel : ViewModel() {

    val productsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var productsPagedList: LiveData<PagedList<ProductModel>>? = null

    fun editWishList(langTag: String, tokenId: String?, productId: String?, doAdd: Boolean) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())
            emitSource(
                ProductRepository(langTag).editWishList(
                    tokenId = tokenId,
                    productId = productId,
                    doAdd = doAdd
                )
            )
        }
}