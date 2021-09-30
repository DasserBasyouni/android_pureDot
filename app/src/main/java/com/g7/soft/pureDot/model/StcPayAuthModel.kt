package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StcPayAuthModel(
    @Json(name = "otpReference") val otpReference: String?,
    @Json(name = "stcpayPmtReference") val stcPayPmtReference: String?,
)