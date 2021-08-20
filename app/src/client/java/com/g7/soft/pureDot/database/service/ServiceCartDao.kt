package com.g7.soft.pureDot.database.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServiceCartDao {
    @Query("SELECT * FROM ServiceCart")
    suspend fun getAll(): List<ServiceCart?>?

    @Query("SELECT * FROM ServiceCart WHERE id = :id")
    suspend fun getItemById(id: Int): ServiceCart?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoProduct(vararg serviceCartItems: ServiceCart?)

    @Query("DELETE FROM ServiceCart WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @get:Query("SELECT COUNT(*) FROM ServiceCart")
    val itemsCount: Int

    @Query("DELETE FROM ServiceCart")
    fun deleteALl()
}