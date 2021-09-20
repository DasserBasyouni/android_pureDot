package com.g7.soft.pureDot.ui.screen.addMoney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.WalletRepository
import kotlinx.coroutines.Dispatchers

class AddMoneyViewModel : ViewModel() {

    val amount = MutableLiveData<String?>()


    fun addMoney(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            WalletRepository(langTag).addMoney(
                tokenId = tokenId,
                amount = amount.value?.toInt(),
            )
        )
    }
}