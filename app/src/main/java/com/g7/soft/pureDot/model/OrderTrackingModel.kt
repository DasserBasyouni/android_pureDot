package com.g7.soft.pureDot.model

import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderTrackingModel(
    @Json(name = "status") val status: Int?,
    @Json(name = "estimatedDate") val estimatedDate: Long?,
){
    val statusAsEnum get() = ApiConstant.OrderStatus.fromInt(status)
    val isDelivered get() = ApiConstant.OrderStatus.fromInt(status) == ApiConstant.OrderStatus.DELIVERED
}