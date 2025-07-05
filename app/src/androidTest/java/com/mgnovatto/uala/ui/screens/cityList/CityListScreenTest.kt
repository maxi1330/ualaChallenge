package com.mgnovatto.uala.ui.screens.cityList

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import com.mgnovatto.uala.MainActivity
import com.mgnovatto.uala.R
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.di.AppModule
import com.mgnovatto.uala.di.DispatchersModule
import com.mgnovatto.uala.di.IoDispatcher
import com.mgnovatto.uala.domain.model.City
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class, DispatchersModule::class)
class CityListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testSchedulerDispatcher = StandardTestDispatcher()

    @BindValue
    @IoDispatcher
    val testDispatcher: CoroutineDispatcher = testSchedulerDispatcher

    @BindValue
    var mockRepository: CityRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun whenScreenStarts_andDownloadSucceeds_thenReadyStateIsShown() = runTest {
        coEvery { mockRepository.downloadCities() } returns true
        coEvery { mockRepository.getPaginatedCities(any(), any()) } returns flowOf(
            PagingData.from(
                listOf(City(1, "Buenos Aires", "AR", -34.6, -58.4, false))
            )
        )

        testSchedulerDispatcher.scheduler.advanceUntilIdle()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("ReadyStateView").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("ReadyStateView").assertIsDisplayed()
    }

    @Test
    fun whenScreenStarts_andDownloadFails_thenErrorStateIsShown() = runTest {
        coEvery { mockRepository.downloadCities() } returns false

        testSchedulerDispatcher.scheduler.advanceUntilIdle()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("ErrorStateView").fetchSemanticsNodes().isNotEmpty()
        }
        val errorMessage = composeTestRule.activity.getString(R.string.download_error_message)
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithTag("retry_button").assertIsDisplayed()
    }

    @Test
    fun givenErrorState_whenRetryIsClicked_thenRepositoryIsCalledAgainAndReadyStateIsShown() = runTest {
        coEvery { mockRepository.downloadCities() } returnsMany listOf(false, true)
        every { mockRepository.getPaginatedCities(any(), any()) } returns flowOf(PagingData.empty())

        testSchedulerDispatcher.scheduler.advanceUntilIdle()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("ErrorStateView").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("retry_button").performClick()

        testSchedulerDispatcher.scheduler.advanceUntilIdle()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("ReadyStateView").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("ReadyStateView").assertIsDisplayed()

        coVerify(exactly = 2) { mockRepository.downloadCities() }
    }
}