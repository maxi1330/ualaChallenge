package com.mgnovatto.uala.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT COUNT(*) FROM cities")
    suspend fun getCityCount(): Int

    @Query("SELECT * FROM cities WHERE name LIKE :query || '%' ORDER BY name COLLATE NOCASE")
    fun getCitiesPagingSource(query: String): PagingSource<Int, CityEntity>

    @Query("SELECT * FROM cities WHERE name LIKE :query || '%' AND isFavorite = 1 ORDER BY name COLLATE NOCASE")
    fun getFavoriteCitiesPagingSource(query: String): PagingSource<Int, CityEntity>

    @Update
    suspend fun updateCity(city: CityEntity)
}