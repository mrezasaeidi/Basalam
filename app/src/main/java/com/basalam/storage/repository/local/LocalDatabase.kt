package com.basalam.storage.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.basalam.storage.repository.local.dao.ProductDao
import com.basalam.storage.repository.local.entity.ProductModel

@Database(entities = [ProductModel::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    fun deleteDatabase() {
        productDao().deleteProducts()
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, LocalDatabase::class.java, "local_database")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}