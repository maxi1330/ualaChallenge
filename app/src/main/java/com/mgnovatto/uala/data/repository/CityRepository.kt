package com.mgnovatto.uala.data.repository

import androidx.paging.PagingData
import com.mgnovatto.uala.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    /**
     * Downloads the list of cities from a remote source and saves them locally.
     *
     * @return true if the download was successful, false otherwise.
     */
    suspend fun downloadCities(): Boolean

    /**
     * Returns a paginated stream of cities, optionally filtered by a search query
     * and a flag for show only favorites.
     *
     * @param query The search query string to filter city names.
     * @param favoritesOnly If true, only favorite cities are included.
     * @return A [Flow] emitting [PagingData] of [City] objects.
     */
    fun getPaginatedCities(query: String, favoritesOnly: Boolean): Flow<PagingData<City>>

    /**
     * Toggles the favorite status of a given city.
     *
     * @param city The [City] whose favorite status should be toggled.
     */
    suspend fun toggleFavorite(city: City)

    /**
     * Retrieves a textual description of a city based on its name and country code.
     *
     * @param cityName The name of the city.
     * @param countryCode The ISO country code of the city.
     * @return A [String] description of the city, or null if not found.
     */
    suspend fun getCityDescription(cityName: String, countryCode: String): String?
}