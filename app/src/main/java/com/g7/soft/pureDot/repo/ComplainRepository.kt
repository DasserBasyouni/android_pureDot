package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class ComplainRepository(private val langTag: String) {

    fun getComplains(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplains(
                tokenId = tokenId,
            )
        }))
    }

    fun addComplain(
        tokenId: String?,
        title: String?,
        description: String?,
        relatedOrderNumber: Int?,
        categoryId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addComplain(
                tokenId = tokenId,
                title = title,
                description = description,
                relatedOrderNumber = relatedOrderNumber,
                categoryId = categoryId,
            )
        }))
    }

    fun getComplainComments(
        tokenId: String?,
        complainId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplainComments(
                tokenId = tokenId,
                complainId = complainId,
            )
        }))
    }
}