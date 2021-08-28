package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class WalletRepository(private val langTag: String) {

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

    fun transferMoney(
        tokenId: String?,
        emailOrPhoneNumber: String?,
        amount: Float?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.transferMoney(
                tokenId = tokenId,
                emailOrPhoneNumber = emailOrPhoneNumber,
                amount = amount,
            )
        }))
    }

    fun replacePoints(
        tokenId: String?,
        amount: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.replacePoints(
                tokenId = tokenId,
                amount = amount,
            )
        }))
    }

    fun suggestContact(
        tokenId: String?,
        emailOrPhoneNumber: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.suggestContact(
                tokenId = tokenId,
                emailOrPhoneNumber = emailOrPhoneNumber
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
                amount = amount
            )
        }))
    }
}