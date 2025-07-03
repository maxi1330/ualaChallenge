package com.mgnovatto.uala.data.repository

import android.util.Log
import com.mgnovatto.uala.data.local.db.CityDao
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.remote.mappers.toEntity
import kotlinx.coroutines.Dispatchers
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
}