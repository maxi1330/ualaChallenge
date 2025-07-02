package com.mgnovatto.uala.data.repository

import android.util.Log
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.store.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDataStore: UserDataStore
) : CityRepository {

    override suspend fun downloadCities(): Boolean {
        return withContext(Dispatchers.IO) {
            val isAlreadyDownloaded = userDataStore.isInitialDownloadComplete.first()

            if (isAlreadyDownloaded) {
                Log.i("CityRepository", "Los datos ya estan descargados.")
                return@withContext true
            }

            try {
                Log.i("CityRepository", "Iniciando descarga...")
                apiService.downloadCities()
                Log.i("CityRepository", "¡Descarga exitosa!")
                userDataStore.setInitialDownloadComplete(true)
                true
            } catch (e: Exception) {
                Log.e("CityRepository", "Error descargando")
                false
            }
        }
    }
}