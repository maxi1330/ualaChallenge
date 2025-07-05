package com.mgnovatto.uala.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Abstract class that defines the Room database for the application.
 */
@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}