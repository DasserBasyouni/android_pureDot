package com.g7.soft.pureDot.repo.flavour

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

open class FlavourWalletRepository(private val langTag: String){

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