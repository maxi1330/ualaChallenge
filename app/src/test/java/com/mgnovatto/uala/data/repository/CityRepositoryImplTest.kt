package com.mgnovatto.uala.data.repository

import android.util.Log
import androidx.paging.PagingSource
import com.mgnovatto.uala.data.local.db.CityDao
import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.data.remote.ApiService
import com.mgnovatto.uala.data.remote.dto.CityDto
import com.mgnovatto.uala.data.remote.dto.CoordinatesDto
import com.mgnovatto.uala.data.remote.dto.WikipediaPageDto
import com.mgnovatto.uala.data.remote.dto.WikipediaQueryDto
import com.mgnovatto.uala.data.remote.dto.WikipediaResponseDto
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.utils.WikiUtils
import com.mgnovatto.uala.utils.getCountryNameFromCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * This class verifies the operation of the CityRepositoryImpl
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CityRepositoryImplTest {

    private val cityApiService: ApiService = mockk()
    private val wikipediaApiService: ApiService = mockk()
    private val cityDao: CityDao = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: CityRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.isLoggable(any(), any()) } returns false

        mockkObject(WikiUtils)
        // Mock the top-level function by mocking its file's generated class name
        mockkStatic("com.mgnovatto.uala.utils.LocaleUtilsKt")

        repository = CityRepositoryImpl(cityApiService, wikipediaApiService, cityDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll() // Clean up all mocks after each test
    }

    // downloadCities Tests

    @Test
    fun `given database is not empty, when downloadCities is called, then it returns true without downloading`() = runTest {
        // Given: The database already has cities.
        coEvery { cityDao.getCityCount() } returns 100

        // When: The initial data setup is called.
        val result = repository.downloadCities()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The result should be true, and the network API should NOT be called.
        assertTrue(result)
        coVerify(exactly = 0) { cityApiService.downloadCities() }
    }

    @Test
    fun `given database is empty and API succeeds, when downloadCities is called, then data is downloaded and inserted`() = runTest {
        // Given: The database is empty and the API will return a list of cities.
        coEvery { cityDao.getCityCount() } returns 0
        val fakeCitiesDto = listOf(CityDto(1, "Test City", "TC", CoordinatesDto(0.0, 0.0)))
        coEvery { cityApiService.downloadCities() } returns fakeCitiesDto
        coEvery { cityDao.insertAll(any()) } returns Unit

        // When: The initial data setup is called.
        val result = repository.downloadCities()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The result should be true, and both the API and DAO should be called.
        assertTrue(result)
        coVerify(exactly = 1) { cityApiService.downloadCities() }
        coVerify(exactly = 1) { cityDao.insertAll(any()) }
    }

    @Test
    fun `given database is empty and API fails, when downloadCities is called, then it returns false`() = runTest {
        // Given: The database is empty and the API call will throw an exception.
        coEvery { cityDao.getCityCount() } returns 0
        coEvery { cityApiService.downloadCities() } throws Exception("Network error")

        // When: The initial data setup is called.
        val result = repository.downloadCities()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The result should be false.
        assertFalse(result)
    }

    // --- getPaginatedCities Tests ---

    @Test
    fun `when getPaginatedCities is called with favoritesOnly false, then it calls getCitiesPagingSource`() = runTest {
        // Given
        val query = "Lon"
        val mockPagingSource: PagingSource<Int, CityEntity> = mockk(relaxed = true)
        every { cityDao.getCitiesPagingSource(query) } returns mockPagingSource

        // When: We collect from the flow to trigger the pagingSourceFactory
        repository.getPaginatedCities(query, favoritesOnly = false).first()

        // Then
        verify(exactly = 1) { cityDao.getCitiesPagingSource(query) }
        verify(exactly = 0) { cityDao.getFavoriteCitiesPagingSource(any()) }
    }

    @Test
    fun `when getPaginatedCities is called with favoritesOnly true, then it calls getFavoriteCitiesPagingSource`() = runTest {
        // Given
        val query = "Lon"
        val mockPagingSource: PagingSource<Int, CityEntity> = mockk(relaxed = true)
        every { cityDao.getFavoriteCitiesPagingSource(query) } returns mockPagingSource

        // When: We collect from the flow to trigger the pagingSourceFactory
        repository.getPaginatedCities(query, favoritesOnly = true).first()

        // Then
        verify(exactly = 1) { cityDao.getFavoriteCitiesPagingSource(query) }
        verify(exactly = 0) { cityDao.getCitiesPagingSource(any()) }
    }

    @Test
    fun `getPaginatedCities returns empty paging source for unmatched input`() = runTest {
        val query = "Zzxxyy"
        val mockPagingSource: PagingSource<Int, CityEntity> = mockk(relaxed = true)
        every { cityDao.getCitiesPagingSource(query) } returns mockPagingSource

        repository.getPaginatedCities(query, favoritesOnly = false).first()

        verify(exactly = 1) { cityDao.getCitiesPagingSource(query) }
    }

    @Test
    fun `getPaginatedCities handles special characters without crashing`() = runTest {
        val query = "!!!"
        val mockPagingSource: PagingSource<Int, CityEntity> = mockk(relaxed = true)
        every { cityDao.getCitiesPagingSource(query) } returns mockPagingSource

        repository.getPaginatedCities(query, favoritesOnly = false).first()

        verify(exactly = 1) { cityDao.getCitiesPagingSource(query) }
    }

    // --- toggleFavorite Tests ---

    @Test
    fun `when toggleFavorite is called, then dao updateCity is called with inverted favorite state`() = runTest {
        // Given: A city that is not a favorite.
        val testCity = City(1, "Test", "TC", 0.0, 0.0, isFavorite = false)
        coEvery { cityDao.updateCity(any()) } returns Unit

        // When: toggleFavorite is called.
        repository.toggleFavorite(testCity)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The DAO updateCity function should be called with the city marked as a favorite.
        coVerify(exactly = 1) { cityDao.updateCity(withArg {
            assertEquals(1, it.id)
            assertTrue(it.isFavorite)
        })}
    }

    // --- getCityDescription Tests ---

    @Test
    fun `given specific wikipedia search succeeds, when getCityDescription is called, then it returns cleaned description`() = runTest {
        // Given: A city and a successful API response
        val rawDescription = "London is the capital [1] of England."
        val cleanedDescription = "London is the capital of England."
        val validResponse = WikipediaResponseDto(query = WikipediaQueryDto(pages = mapOf("1" to WikipediaPageDto("London", rawDescription))))

        every { getCountryNameFromCode("GB") } returns "United Kingdom"
        every { WikiUtils.isValidDescription(rawDescription) } returns true
        every { WikiUtils.cleanWikipediaExtract(rawDescription) } returns cleanedDescription
        coEvery { wikipediaApiService.getWikipediaExtract(titles = "London, United Kingdom") } returns validResponse

        // When we call getCityDescription
        val description = repository.getCityDescription("London", "GB")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then the cleaned description should be returned
        assertEquals(cleanedDescription, description)
        coVerify(exactly = 0) { wikipediaApiService.getWikipediaExtract(titles = "London") }
    }

    @Test
    fun `given specific search fails and general search succeeds, then it returns result from general search`() = runTest {
        // Given: The specific search fails, but the general one succeeds.
        val disambiguationText = "London may refer to:"
        val validText = "A valid description from general search."
        val disambiguationResponse = WikipediaResponseDto(query = WikipediaQueryDto(pages = mapOf("1" to WikipediaPageDto("London", disambiguationText))))
        val validGeneralResponse = WikipediaResponseDto(query = WikipediaQueryDto(pages = mapOf("1" to WikipediaPageDto("London", validText))))

        every { getCountryNameFromCode("GB") } returns "United Kingdom"
        every { WikiUtils.isValidDescription(disambiguationText) } returns false
        every { WikiUtils.isValidDescription(validText) } returns true
        every { WikiUtils.cleanWikipediaExtract(validText) } returns validText
        coEvery { wikipediaApiService.getWikipediaExtract(titles = "London, United Kingdom") } returns disambiguationResponse
        coEvery { wikipediaApiService.getWikipediaExtract(titles = "London") } returns validGeneralResponse

        // When we call getCityDescription
        val description = repository.getCityDescription("London", "GB")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then the valid description should be returned
        assertEquals(validText, description)
        coVerify(exactly = 1) { wikipediaApiService.getWikipediaExtract(titles = "London, United Kingdom") }
        coVerify(exactly = 1) { wikipediaApiService.getWikipediaExtract(titles = "London") }
    }

    @Test
    fun `given both wikipedia searches fail, when getCityDescription is called, then it returns null`() = runTest {
        // Given: Both API calls return invalid descriptions.
        val invalidResponse = WikipediaResponseDto(query = WikipediaQueryDto(pages = mapOf("1" to WikipediaPageDto("London", "may refer to:"))))
        every { getCountryNameFromCode(any()) } returns "Test Country"
        every { WikiUtils.isValidDescription(any()) } returns false
        coEvery { wikipediaApiService.getWikipediaExtract(titles = any()) } returns invalidResponse

        // When we call getCityDescription
        val description = repository.getCityDescription("InvalidCity", "IV")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then the result should be null
        assertNull(description)
    }
}
