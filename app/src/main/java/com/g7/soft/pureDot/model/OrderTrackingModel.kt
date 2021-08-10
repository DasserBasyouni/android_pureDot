package com.g7.soft.pureDot.model

import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderTrackingModel(
    @Json(name = "status") val status: Int?,
    @Json(name = "deliveredEstimatedDateTime") val deliveredEstimatedDateTime: Long?,
    @Json(name = "outForDeliveryEstimatedDateTime") val outForDeliveryEstimatedDateTime: Long?,
    @Json(name = "shippingEstimatedDateTime") val shippingEstimatedDateTime: Long?,
){
    val statusAsEnum get() = ApiConstant.OrderStatus.fromInt(status)
    val isDelivered get() = ApiConstant.OrderStatus.fromInt(status) == ApiConstant.OrderStatus.DELIVERED
}