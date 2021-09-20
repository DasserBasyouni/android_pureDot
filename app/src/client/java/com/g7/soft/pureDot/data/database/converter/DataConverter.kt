package com.g7.soft.pureDot.data.database.converter

import androidx.room.TypeConverter
import com.g7.soft.pureDot.model.ApiShopOrderModel
import com.google.gson.Gson

class DataConverter {
    /*@TypeConverter
    fun fromVariationsIds(date: List<String>?): String? = date?.joinToString()

    @TypeConverter
    fun toVariationsIds(value: String?): List<String>? =
        if (value.isNullOrEmpty()) null else value.split(",").map { it }*/

    @TypeConverter
    fun fromVariationsIds(date: ApiShopOrderModel): String = Gson().toJson(date)

    @TypeConverter
    fun toVariationsIds(value: String): ApiShopOrderModel =
        Gson().fromJson(value, ApiShopOrderModel::class.java)
}