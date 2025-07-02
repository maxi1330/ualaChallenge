package com.mgnovatto.uala.data.repository

interface CityRepository {
    suspend fun downloadCities(): Boolean
}