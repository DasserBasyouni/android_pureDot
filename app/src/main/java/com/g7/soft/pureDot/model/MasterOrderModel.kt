package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MasterOrderModel(
    @Json(name = "number") val number: Int?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "fromAddress") val fromAddress: String?,
    @Json(name = "toAddress") val toAddress: String?,
    @Json(name = "price") val price: Double?,
    @Json(name = "note") val note: String?,
    //@Json(name = "products") val products: List<ProductModel>?,
    @Json(name = "shippingCost") val shippingCost: Double?,
    //@Json(name = "commission") val commission: Double?,
    @Json(name = "clientLat") val clientLat: Double?,
    @Json(name = "clientLng") val clientLng: Double?,
    @Json(name = "clientImageUrl") val clientImageUrl: String?,
    //@Json(name = "status") var status: Int?,
    //@Json(name = "deliveryStatus") var deliveryStatus: Int?,
    //@Json(name = "shopName") val shopName: String?,
    //@Json(name = "shopLat") val shopLat: Double?,
    //@Json(name = "shopLng") val shopLng: Double?,
    @Json(name = "finalRouteImageUrl") val finalRouteImageUrl: String?,
    @Json(name = "address") val address: AddressModel?,
    @Json(name = "clientPhoneNumber") val clientPhoneNumber: String?,
    @Json(name = "review") var review: OrderReviewModel?,
    @Json(name = "orders") var orders: List<OrderModel>?,
) : Parcelable {
    val currency get() = orders?.first()?.currency
    val totalCost get() = orders?.sumOf { it.totalCost ?: 0.0 }
    val totalVat get() = orders?.sumOf { it.totalVat ?: 0.0 }
    val areDelivered get() = orders != null && orders?.filter { ApiConstant.OrderStatus.fromInt(it.status) == ApiConstant.OrderStatus.DELIVERED }?.size == orders?.size
    val productsCount get() = orders?.sumOf { it.products?.size ?: 0 }

    /*val isDelivered get() = ApiConstant.OrderStatus.fromInt(status) == ApiConstant.OrderStatus.DELIVERED
    val isCancelable get() = ApiConstant.OrderStatus.isCancelable(status)
    val isFullyReviewed get() = products?.firstOrNull { it.userReview == null } == null
    val driverTotal get() = shippingCost?.plus(commission ?: 0.0)
    val isDriverPendingOrAccepted get() = ApiConstant.OrderDeliveryStatus.isDriverPendingOrAccepted(deliveryStatus)
    val isNewDeliveryOrder get() = deliveryStatus != ApiConstant.OrderDeliveryStatus.NEW.value
    val deliveryStatusEnum get() = ApiConstant.OrderDeliveryStatus.fromInt(deliveryStatus)*/
}