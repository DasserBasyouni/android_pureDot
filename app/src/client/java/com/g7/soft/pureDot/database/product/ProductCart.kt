package com.g7.soft.pureDot.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProductCart(
    @field:PrimaryKey var id: Int? = null,
    @field:ColumnInfo(name = "productId") var productId: Int,
    /*@ColumnInfo(name = "price") private Float price;*/
    @field:ColumnInfo(name = "quantityInCart") var quantityInCart: Int
)