package com.g7.soft.pureDot.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProductCart(
    @field:PrimaryKey var id: Int? = null,
    @field:ColumnInfo(name = "apiId") var apiId: String? = null,
    @field:ColumnInfo(name = "price") var price: Double,
    @field:ColumnInfo(name = "productId") var productId: String,
    @field:ColumnInfo(name = "quantityInCart") var quantityInCart: Int,
    @field:ColumnInfo(name = "variationsIds") var variationsIds: List<String>?
)