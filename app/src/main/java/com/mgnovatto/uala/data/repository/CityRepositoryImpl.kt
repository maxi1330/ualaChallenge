package com.mgnovatto.uala.data.repository

import android.util.Log
import com.mgnovatto.uala.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CityRepository {

    override suspend fun downloadCities(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("CityRepository", "Iniciando descarga...")
                apiService.downloadCities()
                Log.i("CityRepository", "¡Descarga exitosa!")
                true
            } catch (e: Exception) {
                Log.e("CityRepository", "Error descargando")
                false
            }
        }
    }
}