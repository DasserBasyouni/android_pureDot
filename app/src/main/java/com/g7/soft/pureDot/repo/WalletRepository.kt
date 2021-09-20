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

}