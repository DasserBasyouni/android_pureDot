package com.g7.soft.pureDot.network.response

import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant

data class NetworkRequestResponse<out T>(
    val status: ProjectConstant.Companion.Status,
    val validationError: ProjectConstant.Companion.ValidationError? = null,
    val apiErrorStatus: ApiConstant.Status? = null,
    val exception: Exception? = null,
    val data: T? = null,
) {
    companion object {
        fun <T> invalidInputData(validationError: ProjectConstant.Companion.ValidationError):
                NetworkRequestResponse<T> =
                NetworkRequestResponse(
                        status = ProjectConstant.Companion.Status.VALIDATION_ERROR,
                        validationError = validationError
                )

        fun <T> success(data: T? = null): NetworkRequestResponse<T> =
                NetworkRequestResponse(status = ProjectConstant.Companion.Status.SUCCESS, data = data)

        fun <T> apiError(apiStatus: ApiConstant.Status): NetworkRequestResponse<T> =
                NetworkRequestResponse(
                        status = ProjectConstant.Companion.Status.API_ERROR,
                        apiErrorStatus = apiStatus
                )

        fun <T> error(exception: Exception): NetworkRequestResponse<T> =
                NetworkRequestResponse(
                        status = ProjectConstant.Companion.Status.NETWORK_ERROR,
                        exception = exception
                )

        fun <T> loading(): NetworkRequestResponse<T> =
                NetworkRequestResponse(status = ProjectConstant.Companion.Status.LOADING)
    }
}
