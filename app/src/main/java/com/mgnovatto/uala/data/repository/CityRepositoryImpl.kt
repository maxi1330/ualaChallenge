package com.mgnovatto.uala.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mgnovatto.uala.data.local.db.CityDao
import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.data.local.mappers.toDomain
import com.mgnovatto.uala.data.local.mappers.toEntity
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.remote.mappers.toEntity
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.utils.WikiUtils
import com.mgnovatto.uala.utils.getCountryNameFromCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Implementation of the [CityRepository] interface.
 *
 * Handles city data operations, including:
 * - Downloading and caching city data from a remote API.
 * - Providing paginated access to cities stored locally.
 * - Toggling a city's favorite status.
 * - Fetching Wikipedia descriptions for cities, with a fallback if the full city-country name fails.
 *
 * Uses both a city API and the Wikipedia API via [ApiService] instances.
 */
class CityRepositoryImpl @Inject constructor(
    @Named("CityApiService") private val cityApiService: ApiService,
    @Named("WikipediaApiService") private val wikipediaApiService: ApiService,
    private val cityDao: CityDao,
    ) : CityRepository {

    override suspend fun downloadCities(): Boolean {
        return withContext(Dispatchers.IO) {
            val cityCount = cityDao.getCityCount()

            if (cityCount > 0) {
                return@withContext true
            }

            try {
                val cities = cityApiService.downloadCities()

                val cityEntities = cities.map { it.toEntity() }

                val chunkSize = 200
                cityEntities.chunked(chunkSize).forEach { chunk ->
                    cityDao.insertAll(chunk)
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun getPaginatedCities(query: String, favoritesOnly: Boolean): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                if (favoritesOnly) {
                    cityDao.getFavoriteCitiesPagingSource(query)
                } else {
                    cityDao.getCitiesPagingSource(query)
                }
            }
        ).flow
            .map { pagingData: PagingData<CityEntity> ->
                pagingData.map { cityEntity ->
                    cityEntity.toDomain()
                }
            }
    }

    override suspend fun toggleFavorite(city: City) {
        cityDao.updateCity(city.copy(isFavorite = !city.isFavorite).toEntity())
    }

    override suspend fun getCityDescription(cityName: String, countryCode: String): String? {
        return withContext(Dispatchers.IO) {
            fetchValidDescription("$cityName, ${getCountryNameFromCode(countryCode)}")
                ?: fetchValidDescription(cityName)
        }
    }

    /**
     * Fetches and returns a valid, cleaned description from the Wikipedia API for the given title.
     *
     * This function performs the following steps:
     * - Sends a request to the Wikipedia API using the provided title.
     * - Extracts the summary text (extract) from the first result.
     * - Validates the extract to ensure it's meaningful (not null, empty, or generic).
     * - Cleans the extract using utility functions before returning it.
     *
     * @param titles The title to search for on Wikipedia, typically formatted as `"City, Country"` or simply `"City"`.
     * @return A cleaned and valid description if available, or `null` otherwise.
     */
    private suspend fun fetchValidDescription(titles: String): String? {
        val response = wikipediaApiService.getWikipediaExtract(titles = titles)
        val extract = response.query?.pages?.values?.firstOrNull()?.extract
        return if (WikiUtils.isValidDescription(extract)) {
            WikiUtils.cleanWikipediaExtract(extract)
        } else null
    }
}