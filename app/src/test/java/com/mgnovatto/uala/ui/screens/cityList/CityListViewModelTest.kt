package com.mgnovatto.uala.ui.screens.cityList

import androidx.paging.PagingData
import app.cash.turbine.test
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.domain.model.City
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * This class verifies the operation of the CityListViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    private val mockRepository: CityRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CityListViewModel

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // TESTS

    @Test
    fun `given repository succeeds, when ViewModel inits, then downloadState is Ready`() = runTest {
        // Given: The repository will report a successful download.
        coEvery { mockRepository.downloadCities() } returns true

        // When: The ViewModel is initialized.
        viewModel = CityListViewModel(mockRepository, testDispatcher)

        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The final download state should be Ready.
        assertEquals(DownloadState.Ready, viewModel.downloadState.value)
    }

    @Test
    fun `given repository fails, when ViewModel inits, then downloadState is Error`() = runTest {
        // Given: The repository will report a failed download.
        coEvery { mockRepository.downloadCities() } returns false

        // When: The ViewModel is initialized.
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The final download state should be Error.
        assertEquals(DownloadState.Error, viewModel.downloadState.value)
    }

    @Test
    fun `given download fails, when retry is called, then download is attempted again`() = runTest {
        // Given: The repository will fail the first time, but succeed the second time.
        coEvery { mockRepository.downloadCities() } returnsMany listOf(false, true)
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(DownloadState.Error, viewModel.downloadState.value) // Verify initial failure

        // When: The retry function is called.
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The repository function was called twice and the final state is Ready.
        coVerify(exactly = 2) { mockRepository.downloadCities() }
        assertEquals(DownloadState.Ready, viewModel.downloadState.value)
    }

    @Test
    fun `when onSearchTextChange is called, then searchText state is updated`() = runTest {
        // Given: A successfully initialized ViewModel.
        coEvery { mockRepository.downloadCities() } returns true
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val newText = "London"

        // When: The search text is changed.
        viewModel.onSearchTextChange(newText)

        // Then: The searchText StateFlow should reflect the new value.
        assertEquals(newText, viewModel.searchText.value)
    }

    @Test
    fun `when onFavoritesOnlyChange is called, then favoritesOnly state is updated`() = runTest {
        // Given: A successfully initialized ViewModel.
        coEvery { mockRepository.downloadCities() } returns true
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: The favorites filter is enabled.
        viewModel.onFavoritesOnlyChange(true)

        // Then: The favoritesOnly StateFlow should be true.
        assertEquals(true, viewModel.favoritesOnly.value)
    }

    @Test
    fun `when onToggleFavorite is called, then repository toggleFavorite function is called`() = runTest {
        // Given: A successfully initialized ViewModel and a test city.
        coEvery { mockRepository.downloadCities() } returns true
        val testCity = City(id = 1, name = "Test City", country = "TC", lon = 0.0, lat = 0.0, isFavorite = false)
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: The toggle favorite function is called.
        viewModel.onToggleFavorite(testCity)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Verify that the repository's toggleFavorite function was called exactly once.
        coVerify(exactly = 1) { mockRepository.toggleFavorite(testCity) }
    }

    @Test
    fun `when searchText changes, then getPaginatedCities is called with the new query`() = runTest {
        // Given: A successfully initialized ViewModel.
        coEvery { mockRepository.downloadCities() } returns true
        coEvery { mockRepository.getPaginatedCities(any(), any()) } returns flowOf(PagingData.empty())
        viewModel = CityListViewModel(mockRepository, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.cities.test {
            awaitItem() // Initial emission

            // When: The search text changes.
            viewModel.onSearchTextChange("NewQuery")
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: Verify the repository function was called with the new query.
            coVerify { mockRepository.getPaginatedCities(query = "NewQuery", favoritesOnly = false) }

            cancelAndIgnoreRemainingEvents()
        }
    }
}
