package com.g7.soft.pureDot.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProductCart(
    @field:PrimaryKey var id: Int? = null,
    @field:ColumnInfo(name = "productId") var productId: Int,
    @field:ColumnInfo(name = "price") var price: Double,
    @field:ColumnInfo(name = "quantityInCart") var quantityInCart: Int
)