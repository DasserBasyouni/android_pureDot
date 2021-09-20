package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class ComplaintRepository(private val langTag: String) {

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
        orderId: String?,
        reasonType: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addComplain(
                tokenId = tokenId,
                title = title,
                description = description,
                orderId = orderId,
                reasonType = reasonType,
            )
        }))
    }

    fun getComplainComments(
        tokenId: String?,
        complainId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplainComments(
                tokenId = tokenId,
                complainId = complainId,
            )
        }))
    }

    fun getComplaintOrder(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplaintOrder(
                tokenId = tokenId,
            )
        }))
    }

    fun getComplaintReasons() = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplaintReasons()
        }))
    }


    fun addComplaintReply(
        tokenId: String?,
        message: String?,
        complainId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addComplaintReply(
                tokenId = tokenId,
                message = message,
                complainId = complainId
            )
        }))
    }


    fun rateComplainService(
        tokenId: String?,
        message: String?,
        complainId: String?,
        rating: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.rateComplainService(
                tokenId = tokenId,
                message = message,
                complainId = complainId,
                rating = rating,
            )
        }))
    }
}