package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class GeneralRepository(private val langTag: String) {

    fun getCounties() = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllCounties()
        }))
    }

    fun getCities(
        countryId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllCities(countryId = countryId)
        }))
    }

    fun getZipCodes(
        cityId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllZipCodes(cityId = cityId)
        }))
    }

    fun contactUs(
        tokenId: String?,
        email: String?,
        message: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.contactUs(
                tokenId = tokenId,
                email = email,
                message = message,
            )
        }))
    }
}