package com.g7.soft.pureDot.database.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductCartDao {
    @Query("SELECT * FROM ProductCart")
    suspend fun getAll(): List<ProductCart?>?

    @Query("SELECT * FROM ProductCart WHERE apiId = :apiId")
    suspend fun getItemById(apiId: String): ProductCart?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoProduct(vararg productCartItems: ProductCart?)

    @Query("DELETE FROM ProductCart WHERE apiId = :id")
    suspend fun deleteItem(id: Int)

    @get:Query("SELECT COUNT(*) FROM ProductCart")
    val itemsCount: Int

    @Query("DELETE FROM ProductCart")
    fun deleteALl()
}