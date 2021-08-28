package com.g7.soft.pureDot.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.model.ApiResponseModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class NetworkRequestHandler {

    suspend fun <T> handle(request: suspend () -> ApiResponseModel<T>?): LiveData<NetworkRequestResponse<T>> = liveData(Dispatchers.IO) {
        try {
            val networkRequest = request()

            networkRequest?.status
                    ?: emit(NetworkRequestResponse.apiError<T>(apiStatus = ApiConstant.Status.NULL_STATUS))

            if (networkRequest!!.status == ApiConstant.Status.SUCCESS.value)
                emit(NetworkRequestResponse.success(data = networkRequest.data))
            else
                emit(NetworkRequestResponse.apiError<T>(apiStatus = ApiConstant.Status.fromInt(networkRequest.status!!)))

        } catch (exception: Exception) {
            Log.e("Z_", "ee: $exception")
            Timber.e("NetworkRequestHandler: handle catch", exception)
            emit(NetworkRequestResponse.error<T>(exception = exception))
        }
    }

}