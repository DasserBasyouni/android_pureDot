package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ServiceModel(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "price") val price: Double?,
    @Json(name = "shop") val shop: StoreModel?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "shortDescription") val shortDescription: String?,
) : Parcelable