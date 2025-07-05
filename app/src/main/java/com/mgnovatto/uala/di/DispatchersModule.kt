package com.mgnovatto.uala.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/**
 * Provides coroutine dispatchers for dependency injection.
 *
 * This module installs into the SingletonComponent, making the provided dispatcher
 * available throughout the application's lifecycle. It uses the [IoDispatcher] qualifier
 * to distinguish the IO dispatcher binding.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    /**
     * Provides a [CoroutineDispatcher] optimized for IO-bound operations such as
     * reading from or writing to the disk or network.
     *
     * Annotated with [IoDispatcher] to differentiate it from other dispatchers.
     */
    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}