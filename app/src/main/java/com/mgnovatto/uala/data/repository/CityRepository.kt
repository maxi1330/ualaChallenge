package com.mgnovatto.uala.data.repository

import androidx.paging.PagingData
import com.mgnovatto.uala.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun downloadCities(): Boolean
    fun getPaginatedCities(query: String, favoritesOnly: Boolean): Flow<PagingData<City>>
    suspend fun toggleFavorite(city: City)

    suspend fun getCityDescription(cityName: String, countryCode: String): String?
}