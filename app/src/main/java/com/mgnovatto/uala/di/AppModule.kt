package com.mgnovatto.uala.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.data.repository.CityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCityRepository(api: ApiService): CityRepository {
        return CityRepositoryImpl(api)
    }
}