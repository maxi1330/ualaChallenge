package com.mgnovatto.uala.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mgnovatto.uala.BuildConfig
import com.mgnovatto.uala.data.local.db.AppDatabase
import com.mgnovatto.uala.data.local.db.CityDao
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.data.repository.CityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("CityApiService")
    fun provideCityApiService(): ApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("WikipediaApiService")
    fun provideWikipediaApiService(): ApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://es.wikipedia.org/w/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cities_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCityDao(database: AppDatabase): CityDao {
        return database.cityDao()
    }

    @Provides
    @Singleton
    fun provideCityRepository(
        @Named("CityApiService") cityApi: ApiService,
        @Named("WikipediaApiService") wikipediaApi: ApiService,
        cityDao: CityDao
    ): CityRepository {
        return CityRepositoryImpl(cityApi, wikipediaApi, cityDao)
    }

    @Provides
    @Named("dispatcher")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}