package com.mgnovatto.uala.ui.screens.cityDetail

import app.cash.turbine.test
import com.mgnovatto.uala.data.repository.CityRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * This class verifies the operation of the CityDetailViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CityDetailViewModelTest {

    private val mockRepository: CityRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CityDetailViewModel

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CityDetailViewModel(mockRepository)
    }

    /**
     * Cleans up the test environment after each test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //TESTS

    @Test
    fun `given repository returns a description, when fetchCityDescription is called, then description state is updated`() = runTest {
        // Given: The repository will return a specific description.
        val cityName = "London"
        val countryCode = "GB"
        val expectedDescription = "London is the capital of England."
        coEvery { mockRepository.getCityDescription(cityName, countryCode) } returns expectedDescription

        // When: The fetch function is called with custom texts.
        viewModel.fetchCityDescription(
            cityName = cityName,
            countryCode = countryCode,
            loadingText = "Loading...",
            notFoundText = "Not Found"
        )
        // Advance the dispatcher to allow the coroutine to execute.
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The description StateFlow should hold the expected value from the repository.
        assertEquals(expectedDescription, viewModel.description.value)
    }

    @Test
    fun `given repository returns null, when fetchCityDescription is called, then description state shows passed error message`() = runTest {
        // Given: The repository will fail to find a description (returns null).
        val cityName = "UnknownCity"
        val countryCode = "XX"
        val expectedErrorMessage = "Description for this city was not found."
        coEvery { mockRepository.getCityDescription(cityName, countryCode) } returns null

        // When: The fetch function is called with custom texts.
        viewModel.fetchCityDescription(
            cityName = cityName,
            countryCode = countryCode,
            loadingText = "Loading...",
            notFoundText = expectedErrorMessage
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The description StateFlow should hold the specific error message that was passed in.
        assertEquals(expectedErrorMessage, viewModel.description.value)
    }

    @Test
    fun `when fetchCityDescription is called, then description state transitions correctly`() = runTest {
        // Given: The repository will eventually return a description.
        val cityName = "Paris"
        val countryCode = "FR"
        val finalDescription = "Paris is the capital of France."
        val loadingMessage = "Fetching data, please wait..."
        coEvery { mockRepository.getCityDescription(cityName, countryCode) } returns finalDescription

        // Use Turbine to test the flow of state emissions
        viewModel.description.test {
            // The initial state is an empty string
            assertEquals("", awaitItem())

            // When: The fetch function is called.
            viewModel.fetchCityDescription(
                cityName = cityName,
                countryCode = countryCode,
                loadingText = loadingMessage,
                notFoundText = "Not Found"
            )

            // Then: We expect the loading state, followed by the final state.
            assertEquals(loadingMessage, awaitItem())
            assertEquals(finalDescription, awaitItem())
        }
    }
}
