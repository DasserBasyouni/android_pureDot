package com.g7.soft.pureDot.ui.screen.transferMoney

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.ContactModel
import com.g7.soft.pureDot.model.WalletDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.WalletRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class TransferMoneyViewModel(internal val tokenId: String?) : ViewModel() {


    val emailOrPhoneNumber = MutableLiveData<String?>()
    val amount = MutableLiveData<String?>()

    val walletResponse = MediatorLiveData<NetworkRequestResponse<WalletDataModel>>()
    val walletLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val contactsResponse = MediatorLiveData<NetworkRequestResponse<ContactModel?>?>()
    val contactsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    fun getWalletData(langTag: String, tokenId: String?) {
        walletResponse.value = NetworkRequestResponse.loading()
        walletResponse.apply {
            this.addSource(WalletRepository(langTag).getWalletData(tokenId = tokenId)) {
                walletResponse.value = it
            }
        }
    }

    fun suggestContact(langTag: String, tokenId: String?) {
        contactsResponse.value = NetworkRequestResponse.loading()
        contactsResponse.apply {
            this.addSource(
                WalletRepository(langTag).suggestContact(
                    tokenId = tokenId,
                    emailOrPhoneNumber = emailOrPhoneNumber.value
                )
            ) {
                contactsResponse.value = it
            }
        }
    }

    fun transferMoney(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils()
            .setPhoneNumberOrEmail(emailOrPhoneNumber.value)
            .setAmount(amount.value)
            .getError()?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            WalletRepository(langTag).transferMoney(
                tokenId = tokenId,
                emailOrPhoneNumber = emailOrPhoneNumber.value,
                amount = amount.value?.toFloat()
            )
        )
    }
}