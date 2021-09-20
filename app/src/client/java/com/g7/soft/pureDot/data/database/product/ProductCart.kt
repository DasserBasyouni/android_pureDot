package com.g7.soft.pureDot.data.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.g7.soft.pureDot.model.ApiShopOrderModel

@Entity
class ProductCart(
    @field:PrimaryKey var id: Int? = null,
    @field:ColumnInfo(name = "apiShopOrder") var apiShopOrder: ApiShopOrderModel,
)