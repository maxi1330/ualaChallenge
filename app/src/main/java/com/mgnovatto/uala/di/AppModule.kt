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

/**
 * Hilt module that provides singleton-scoped dependencies used throughout the application.
 *
 * This includes:
 * - Retrofit services for city and Wikipedia APIs
 * - Room database instance and DAO
 * - CityRepository implementation
 * - Coroutine dispatcher for background operations
 *
 * All provided dependencies are scoped to the application's lifecycle via [SingletonComponent].
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton [ApiService] instance for fetching city data from the configured City File.
     *
     * Uses Retrofit and kotlinx.serialization for JSON conversion, and is distinguished by the
     * name "CityApiService" to allow multiple Retrofit instances within the application.
     *
     * @return A configured [ApiService] instance for city data.
     */
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

    /**
     * Provides a singleton [ApiService] instance for accessing Wikipedia content.
     *
     * This service uses a separate base URL and is identified by the name "WikipediaApiService"
     * to distinguish it from other Retrofit instances.
     *
     * @return A configured [ApiService] instance for Wikipedia queries.
     */
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

    /**
     * Provides a singleton instance of [AppDatabase] using Room.
     *
     * This database stores city information locally and is scoped to the application context.
     *
     * @param context The application context used to build the database.
     * @return A configured [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cities_db"
        ).build()
    }

    /**
     * Provides a singleton instance of [CityDao] to access city data from the Room database.
     *
     * @param database The [AppDatabase] instance from which the DAO is retrieved.
     * @return The [CityDao] used for accessing city-related database operations.
     */
    @Provides
    @Singleton
    fun provideCityDao(database: AppDatabase): CityDao {
        return database.cityDao()
    }

    /**
     * Provides a singleton implementation of [CityRepository].
     *
     * Combines both Retrofit services and the local DAO to support city data operations,
     * such as loading from network and caching locally.
     *
     * @param cityApi Retrofit service for loading city data from a Gist.
     * @param wikipediaApi Retrofit service for loading city descriptions from Wikipedia.
     * @param cityDao DAO for accessing cached city data in the local database.
     * @return An implementation of [CityRepository].
     */
    @Provides
    @Singleton
    fun provideCityRepository(
        @Named("CityApiService") cityApi: ApiService,
        @Named("WikipediaApiService") wikipediaApi: ApiService,
        cityDao: CityDao
    ): CityRepository {
        return CityRepositoryImpl(cityApi, wikipediaApi, cityDao)
    }

    /**
     * Provides a coroutine [CoroutineDispatcher] optimized for I/O operations.
     *
     * Annotated with @Named("dispatcher") to distinguish it from other dispatchers.
     *
     * @return The [Dispatchers.IO] dispatcher.
     */
    @Provides
    @Named("dispatcher")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}