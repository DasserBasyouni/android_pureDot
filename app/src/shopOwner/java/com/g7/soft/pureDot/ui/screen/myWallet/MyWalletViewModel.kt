package com.g7.soft.pureDot.ui.screen.myWallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.TransactionModel
import com.g7.soft.pureDot.model.WalletDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.WalletRepository

class MyWalletViewModel(internal val tokenId: String?) : ViewModel() {

    val walletResponse = MediatorLiveData<NetworkRequestResponse<WalletDataModel>>()
    val walletLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val transactionLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var transactionPagedList: LiveData<PagedList<TransactionModel>>? = null



    fun getWalletData(langTag: String, tokenId: String?) {
        walletResponse.value = NetworkRequestResponse.loading()
        walletResponse.apply {
            this.addSource(WalletRepository(langTag).getWalletData(tokenId = tokenId)) {
                walletResponse.value = it
            }
        }
    }
}