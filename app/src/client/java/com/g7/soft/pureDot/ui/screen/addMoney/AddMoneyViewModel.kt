package com.g7.soft.pureDot.ui.screen.addMoney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.WalletRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class AddMoneyViewModel : ViewModel() {

    val amount = MutableLiveData<String?>()


    fun addMoney(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .setAmount(amount.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            WalletRepository(langTag).addMoney(
                tokenId = tokenId,
                amount = amount.value?.toInt(),
            )
        )
    }
}