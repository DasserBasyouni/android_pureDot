package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MasterOrderModel(
    @Json(name = "id") var id: String?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "number") var number: Int?,
    @Json(name = "isService") val isService: Boolean?,
    //@Json(name = "fromAddress") val fromAddress: String?,
    //@Json(name = "toAddress") val toAddress: String?,

    @Json(name = "clientImageUrl") val clientImageUrl: String?,

    @Json(name = "clientAddress") val clientAddress: AddressModel?,
    @Json(name = "orders") var orders: MutableList<OrderModel>?,

    @Json(name = "clientFirstName") val clientFirstName: String?,
    @Json(name = "clientLastName") val clientLastName: String?,
    @Json(name = "clientEmail") val clientEmail: String?,
    @Json(name = "clientPhoneNumber") val clientPhoneNumber: String?,
    @Json(name = "clientNotes") val clientNotes: String?,

    @Json(name = "couponCode") val couponCode: String?,

    @Json(name = "servants") val servants: Int?,
    @Json(name = "serviceDate") val serviceDate: Long?,
    @Json(name = "servantsTotalCost") val servantsTotalCost: Double?,

    @Json(name = "shippingCost") val shippingCost: Double?,
    @Json(name = "subTotal") val subTotal: Double?,
    @Json(name = "vat") val vat: Double?,
    @Json(name = "totalCouponDiscount") val totalCouponDiscount: Double?,
    @Json(name = "commission") val commission: Double?, // in driver and shop versions only
    @Json(name = "totalOrderCost") var totalOrderCost: Double?,

    @Json(name = "notes") val notes: String?,
) : Parcelable {
    //val currency get() = orders?.first()?.currency
    /*val subTotalCost get() = orders?.sumOf { it.subTotal ?: 0.0 }
    val totalCost get() = orders?.sumOf { it.totalCost ?: 0.0 }
    val totalVat get() = orders?.sumOf { it.vat ?: 0.0 }*/
    val clientFullName = "$clientFirstName $clientLastName"
    val firstOrder = orders?.first()
    val doShopOwnerHaveAction get() = ApiConstant.OrderStatus.doShopOwnerHaveAction(firstOrder?.status)

    val isCancelable: Boolean
        get() = orders != null && orders?.filter {
            ApiConstant.OrderStatus.fromInt(it.status) == ApiConstant.OrderStatus.NEW
        }?.size == orders?.size
    val isDelivered
        get() = orders != null && orders?.filter {
            ApiConstant.OrderStatus.fromInt(it.status) == ApiConstant.OrderStatus.DELIVERED
        }?.size == orders?.size
    val productsCount get() = orders?.sumOf { it.products?.size ?: 0 }

    /*// todo change name to nameWithVariations when api ready
    val productsNamesWithVariations = orders?.map {
        it.products?.map { product -> product.name }?.joinToString(", ")
    }?.joinToString(", ")*/
    /*val isDelivered get() = ApiConstant.OrderStatus.fromInt(status) == ApiConstant.OrderStatus.DELIVERED
    val isCancelable get() = ApiConstant.OrderStatus.isCancelable(status)
    val isFullyReviewed get() = products?.firstOrNull { it.userReview == null } == null
    val driverTotal get() = shippingCost?.plus(commission ?: 0.0)
    val isDriverPendingOrAccepted get() = ApiConstant.OrderDeliveryStatus.isDriverPendingOrAccepted(deliveryStatus)
    val isNewDeliveryOrder get() = deliveryStatus != ApiConstant.OrderDeliveryStatus.NEW.value
    val deliveryStatusEnum get() = ApiConstant.OrderDeliveryStatus.fromInt(deliveryStatus)*/
}