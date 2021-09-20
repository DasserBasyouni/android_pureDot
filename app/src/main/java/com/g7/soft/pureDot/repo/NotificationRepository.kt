package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class NotificationRepository(private val langTag: String) {

    fun getNotifications(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getNotifications(
                tokenId = tokenId,
            )
        }))
    }

    fun doNotify(
        tokenId: String?,
        doNotify: Boolean?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.doNotify(
                tokenId = tokenId,
                doNotify = doNotify,
            )
        }))
    }

}