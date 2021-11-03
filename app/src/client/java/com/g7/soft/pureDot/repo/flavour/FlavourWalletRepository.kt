package com.g7.soft.pureDot.repo.flavour

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

open class FlavourWalletRepository(private val langTag: String) {

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

    fun replacePoints(
        tokenId: String?,
        amount: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.replacePoints(
                tokenId = tokenId,
                amount = amount
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
}