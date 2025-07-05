package com.mgnovatto.uala.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Represents a city entity stored in the local Room database.
 */
@Entity(
    tableName = "cities",
    indices = [Index(value = ["name"])]
)
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val isFavorite: Boolean = false
)