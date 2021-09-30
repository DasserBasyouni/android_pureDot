package com.g7.soft.pureDot.repo

import android.util.Log
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.WorkingDayModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

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
        workingDays: List<WorkingDayModel>?,
    ) = liveData(Dispatchers.IO) {
        val jsonObject = JSONObject()
        jsonObject.put("tokenId", tokenId)
        jsonObject.put("workingHours", JSONArray().apply {
            for (workingDay in workingDays ?: listOf())
                this.put(JSONObject().apply {
                    this.put("day", workingDay.day)
                    this.put("isEnabled", workingDay.isEnabled)
                    this.put("times", JSONArray().apply {
                        for (time in workingDay.times ?: listOf())
                            this.put(JSONObject().apply {
                                this.put("id", time.id)
                                this.put("fromTime", time.fromTime)
                                this.put("toTime", time.toTime)
                            })
                    })
                })
        })
        val body = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("ZZ_", "body: ${jsonObject.toString()}")

        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addEditWorkingHours(body)
        }))
    }
}