package com.g7.soft.pureDot.database.converter

import androidx.room.TypeConverter

class DataConverter {
    @TypeConverter
    fun fromVariationsIds(date: List<String>?): String? = date?.joinToString()

    @TypeConverter
    fun toVariationsIds(value: String?): List<String>? =
        if (value.isNullOrEmpty()) null else value.split(",").map { it }
}