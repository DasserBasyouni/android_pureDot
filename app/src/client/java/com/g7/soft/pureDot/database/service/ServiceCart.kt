package com.g7.soft.pureDot.database.service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ServiceCart(
    @field:PrimaryKey var id: Int? = null,
    @field:ColumnInfo(name = "productId") var productId: Int,
    /*@ColumnInfo(name = "price") private Float price;*/
    @field:ColumnInfo(name = "quantityInCart") var quantityInCart: Int
)