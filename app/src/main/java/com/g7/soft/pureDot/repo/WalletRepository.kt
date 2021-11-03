package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import com.g7.soft.pureDot.repo.flavour.FlavourWalletRepository
import kotlinx.coroutines.Dispatchers

class WalletRepository(private val langTag: String): FlavourWalletRepository(langTag) {

    fun getWalletData(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getWalletData(
                tokenId = tokenId,
            )
        }))
    }

    fun getTransactions(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getTransactions(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber
            )
        }))
    }

    fun addMoneyIsPaid(
        tokenId: String?,
        amount: Double?,
        paymentMethod: String?,
        masterOrderId: String?,
        isPaid: Boolean?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addMoneyIsPaid(
                tokenId = tokenId,
                amount = amount,
                paymentMethod = paymentMethod,
                masterOrderId = masterOrderId,
                isPaid = isPaid,
            )
        }))
    }

    fun sendVerificationMoneyTransfer(tokenId: String?) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.sendVerificationMoneyTransfer(tokenId = tokenId)
        }))
    }

    fun verifyMoneyTransfer(
        tokenId: String?,
        verificationCode: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.verifyMoneyTransfer(
                    tokenId = tokenId,
                    verificationCode = verificationCode
                )
        }))
    }

    fun addMoney(
        tokenId: String?,
        amount: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addMoney(
                tokenId = tokenId,
                amount = amount,
            )
        }))
    }
}