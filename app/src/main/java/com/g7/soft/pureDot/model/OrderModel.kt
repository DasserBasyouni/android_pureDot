package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class OrderModel(
    @Json(name = "id") val id: String?,
    @Json(name = "number") val number: Int?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "shop") val shop: StoreModel?,
    @Json(name = "branch") val branch: BranchModel?,

    @Json(name = "fromAddress") val fromAddress: String?,
    @Json(name = "toAddress") val toAddress: String?,

    @Json(name = "finalRouteImageUrl") val finalRouteImageUrl: String?,
    @Json(name = "isReturn") val isReturn: Boolean?,

    @Json(name = "products") val products: MutableList<ProductModel>?,
    @Json(name = "userReview") var userReview: OrderReviewModel?,

    @Json(name = "status") var status: Int?,
    @Json(name = "deliveryStatus") var deliveryStatus: Int?,

    @Json(name = "shippingCost") val shippingCost: Double?,
    @Json(name = "commission") val commission: Double?,
    @Json(name = "subTotal") val subTotal: Double?,
    @Json(name = "vat") val vat: Double?,
    @Json(name = "totalOrderCost") val totalOrderCost: Double?,

    ) : Parcelable {
    //val currency get() = products?.first()?.currency
    val isDelivered get() = ApiConstant.OrderStatus.fromInt(status) == ApiConstant.OrderStatus.DELIVERED
    val isCancelable get() = ApiConstant.OrderStatus.isCancelable(status)

    //val isFullyReviewed get() = products?.firstOrNull { it.userReview == null } == null
    val driverTotal get() = shippingCost?.plus(commission ?: 0.0)
    val isDriverPendingOrAccepted
        get() = ApiConstant.OrderDeliveryStatus.isDriverPendingOrAccepted(
            deliveryStatus
        )
    val isNewDeliveryOrder get() = deliveryStatus != ApiConstant.OrderDeliveryStatus.NEW.value
    val deliveryStatusEnum get() = ApiConstant.OrderDeliveryStatus.fromInt(deliveryStatus)
}