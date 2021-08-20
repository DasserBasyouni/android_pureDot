package com.g7.soft.pureDot.database.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ServiceCart::class], version = 12, exportSchema = false)
internal abstract class ServiceCartDatabase : RoomDatabase() {

    abstract fun cartDao(): ServiceCartDao?

    companion object {
        @Volatile
        private var INSTANCE: ServiceCartDatabase? = null
        fun getDatabase(context: Context): ServiceCartDatabase? {
            if (INSTANCE == null) {
                synchronized(ServiceCartDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ServiceCartDatabase::class.java, "service_cart"
                        )
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