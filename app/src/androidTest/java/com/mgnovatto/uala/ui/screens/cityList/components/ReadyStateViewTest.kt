package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.theme.UalaChallengeTheme
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [ReadyStateView] Composable.
 * This class verifies that the UI displays correctly and that user interactions
 * trigger the appropriate callbacks.
 */
class ReadyStateViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper function to set up the test environment
    private fun setupTest(
        initialCities: List<City> = emptyList(),
        onSearchTextChange: (String) -> Unit = {},
        onFavoritesOnlyChange: (Boolean) -> Unit = {},
        onToggleFavorite: (City) -> Unit = {},
        onCityClick: (City) -> Unit = {},
        onInfoClick: (City) -> Unit = {}
    ) {
        // Create a Flow of PagingData from our fake list
        val citiesFlow = MutableStateFlow(PagingData.from(initialCities))

        composeTestRule.setContent {
            // We need to collect the flow as LazyPagingItems within a Composable
            val lazyPagingItems = citiesFlow.collectAsLazyPagingItems()
            val (searchText, setSearchText) = remember { mutableStateOf("") }
            val (favoritesOnly, setFavoritesOnly) = remember { mutableStateOf(false) }

            UalaChallengeTheme {
                ReadyStateView(
                    cities = lazyPagingItems,
                    searchText = searchText,
                    favoritesOnly = favoritesOnly,
                    onSearchTextChange = {
                        setSearchText(it)
                        onSearchTextChange(it)
                    },
                    onFavoritesOnlyChange = {
                        setFavoritesOnly(it)
                        onFavoritesOnlyChange(it)
                    },
                    onToggleFavorite = onToggleFavorite,
                    onCityClick = onCityClick,
                    onInfoClick = onInfoClick
                )
            }
        }
    }

    @Test
    fun givenAListOfCities_whenReadyStateViewIsDisplayed_thenCitiesAreShown() {
        // Given
        val fakeCities = listOf(
            City(1, "Buenos Aires", "AR", 0.0, 0.0, false),
            City(2, "London", "GB", 0.0, 0.0, true)
        )
        setupTest(initialCities = fakeCities)

        // Then
        composeTestRule.onNodeWithText("Buenos Aires, AR").assertIsDisplayed()
        composeTestRule.onNodeWithText("London, GB").assertIsDisplayed()
    }

    @Test
    fun whenUserTypesInSearchBar_thenCallbackIsCalled() {
        // Given
        var searchText = ""
        setupTest(onSearchTextChange = { searchText = it })

        // When
        composeTestRule.onNodeWithContentDescription("Buscar").performClick() // To focus
        composeTestRule.onNodeWithText("Buscar ciudad...").performTextInput("Test")

        // Then
        assertEquals("Test", searchText)
    }

    @Test
    fun whenUserClicksOnInfo_thenCallbackIsCalled() {
        // Given
        var clickedCity: City? = null
        val fakeCity = City(1, "Buenos Aires", "AR", 0.0, 0.0, false)
        setupTest(
            initialCities = listOf(fakeCity),
            onInfoClick = { clickedCity = it }
        )

        // When
        // We find the info icon associated with the "Buenos Aires" row
        composeTestRule.onNodeWithContentDescription("Información").performClick()

        // Then
        assertEquals(fakeCity, clickedCity)
    }
}
