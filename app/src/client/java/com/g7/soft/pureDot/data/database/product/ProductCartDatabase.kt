package com.g7.soft.pureDot.data.database.product

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.g7.soft.pureDot.data.database.converter.DataConverter

@Database(entities = [ProductCart::class], version = 15, exportSchema = false)
@TypeConverters(DataConverter::class)
internal abstract class ProductCartDatabase : RoomDatabase() {

    abstract fun cartDao(): ProductCartDao?

    companion object {
        @Volatile
        private var INSTANCE: ProductCartDatabase? = null
        fun getDatabase(context: Context): ProductCartDatabase? {
            if (INSTANCE == null) {
                synchronized(ProductCartDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ProductCartDatabase::class.java, "product_cart"
                        )
                            //@ProvidedTypeConverter && .addTypeConverter(DataConverter())
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val callback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}