package com.mgnovatto.uala.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mgnovatto.uala.data.local.db.CityDao
import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.data.local.mappers.toEntity
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.remote.mappers.toEntity
import com.mgnovatto.uala.data.local.mappers.toDomain
import com.mgnovatto.uala.domain.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cityDao: CityDao
) : CityRepository {

    override suspend fun downloadCities(): Boolean {
        return withContext(Dispatchers.IO) {
            val cityCount = cityDao.getCityCount()

            if (cityCount > 0) {
                Log.i("CityRepository", "Los datos ya estan descargados.")
                return@withContext true
            }

            try {
                Log.i("CityRepository", "Iniciando descarga...")
                val cities = apiService.downloadCities()

                val cityEntities = cities.map { it.toEntity() }
                cityDao.insertAll(cityEntities)

                Log.i("CityRepository", "¡Descarga y guardado en DB exitosos!")
                true
            } catch (e: Exception) {
                Log.e("CityRepository", "Error descargando")
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
}