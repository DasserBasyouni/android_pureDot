package com.g7.soft.pureDot.ui.screen.bankAccount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers

class BankAccountViewModel : ViewModel() {

    val bankName = MutableLiveData<String?>()
    val iban = MutableLiveData<String?>()


    fun save(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).updateBankAccount(
                tokenId = tokenId,
                bankName = bankName.value,
                iban = iban.value,
            )
        )
    }
}