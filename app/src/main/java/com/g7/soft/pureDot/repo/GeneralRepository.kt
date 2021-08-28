package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class GeneralRepository(private val langTag: String) {

    fun getSignUpFields() = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getSignUpFields()
        }))
    }

    fun getCounties() = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllCounties()
        }))
    }

    fun getCities(
        countryId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllCities(countryId = countryId)
        }))
    }

    fun getZipCodes(
        cityId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAllZipCodes(cityId = cityId)
        }))
    }

    fun contactUs(
        name: String?,
        email: String?,
        message: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.contactUs(
                name = name,
                email = email,
                message = message,
            )
        }))
    }

    fun changeLanguage(
        fcmToken: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.changeLanguage(fcmToken = fcmToken)
        }))
    }
}