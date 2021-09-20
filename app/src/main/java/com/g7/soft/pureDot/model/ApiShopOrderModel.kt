package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ApiShopOrderModel(
    @Json(name = "storeId") val shopId: String?,
    @Json(name = "branchId") val branchId: String?,
    @Json(name = "items") val items: MutableList<ApiShopOrderItemModel>?
) : Parcelable