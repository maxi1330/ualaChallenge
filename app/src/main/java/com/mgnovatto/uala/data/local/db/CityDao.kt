package com.mgnovatto.uala.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object (DAO) for performing operations on the `cities` table.
 */
@Dao
interface CityDao {
    /**
     * Inserts a list of cities into the database.
     * If a city with the same primary key already exists, it will be replaced.
     *
     * @param cities The list of cities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    /**
     * Returns the total number of city entries in the database.
     *
     * @return The total count of cities.
     */
    @Query("SELECT COUNT(*) FROM cities")
    suspend fun getCityCount(): Int

    /**
     * Returns a PagingSource for all cities whose names start with the given query string.
     * The results are ordered alphabetically, case-insensitive.
     *
     * @param query The query string to match against city names.
     * @return A PagingSource of CityEntity.
     */
    @Query("SELECT * FROM cities WHERE name LIKE :query || '%' ORDER BY name COLLATE NOCASE")
    fun getCitiesPagingSource(query: String): PagingSource<Int, CityEntity>

    /**
     * Returns a PagingSource for favorite cities whose names start with the given query string.
     * The results are ordered alphabetically, case-insensitive.
     *
     * @param query The query string to match against favorite city names.
     * @return A PagingSource of CityEntity marked as favorites.
     */
    @Query("SELECT * FROM cities WHERE name LIKE :query || '%' AND isFavorite = 1 ORDER BY name COLLATE NOCASE")
    fun getFavoriteCitiesPagingSource(query: String): PagingSource<Int, CityEntity>

    /**
     * Updates the details of a single city in the database.
     *
     * @param city The CityEntity object containing updated information.
     */
    @Update
    suspend fun updateCity(city: CityEntity)
}