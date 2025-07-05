package com.mgnovatto.uala.ui.screens.cityDetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.di.AppModule
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.theme.UalaChallengeTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * This class uses Hilt to replace the real repository with a mock,
 * allowing us to test the UI's reaction to different data states.
 */
@HiltAndroidTest
@UninstallModules(AppModule::class)
class CityDetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @BindValue
    var mockRepository: CityRepository = mockk(relaxed = true)

    private val fakeCity = City(1, "La Plata", "AR", -34.9214, -57.9544, false)

    private lateinit var viewModel: CityDetailViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = CityDetailViewModel(mockRepository)
    }

    @Test
    fun whenDescriptionIsFound_thenItIsDisplayedOnScreen() {
        val expectedDescription = "La Plata es la capital de la provincia de Buenos Aires."
        coEvery {
            mockRepository.getCityDescription(fakeCity.name, fakeCity.country)
        } returns expectedDescription

        composeTestRule.setContent {
            UalaChallengeTheme {
                CityDetailScreen(
                    city = fakeCity,
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText(expectedDescription).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(expectedDescription).assertIsDisplayed()
    }

    @Test
    fun whenDescriptionIsNotFound_thenNotFoundMessageIsDisplayed() {
        // Given: The repository will fail to find a description (returns null).
        coEvery {
            mockRepository.getCityDescription(fakeCity.name, fakeCity.country)
        } returns null

        // When: The CityDetailScreen is launched
        composeTestRule.setContent {
            UalaChallengeTheme {
                CityDetailScreen(
                    city = fakeCity,
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then: Wait until the "not found" message appears
        val notFoundText = "No se encontró descripción para esta ciudad."
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText(notFoundText).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(notFoundText).assertIsDisplayed()
    }
}
