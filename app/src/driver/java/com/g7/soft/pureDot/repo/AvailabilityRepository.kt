package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.WorkingHourModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class AvailabilityRepository(private val langTag: String) {

    fun changeAvailability(
        tokenId: String?,
        isAvailable: Boolean?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.changeAvailability(
                tokenId = tokenId,
                isAvailable = isAvailable,
            )
        }))
    }

    fun checkAvailability(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkAvailability(
                tokenId = tokenId,
            )
        }))
    }

    fun getWorkingHours(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getWorkingHours(
                tokenId = tokenId,
            )
        }))
    }

    fun addEditWorkingHours(
        tokenId: String?,
        saturday: List<WorkingHourModel>?,
        sunday: List<WorkingHourModel>?,
        monday: List<WorkingHourModel>?,
        tuesday: List<WorkingHourModel>?,
        wednesday: List<WorkingHourModel>?,
        thursday: List<WorkingHourModel>?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addEditWorkingHours(
                tokenId = tokenId,
                saturday = saturday,
                sunday = sunday,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday
            )
        }))
    }
}