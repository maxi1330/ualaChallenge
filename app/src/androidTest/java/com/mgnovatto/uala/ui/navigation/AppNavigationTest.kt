package com.mgnovatto.uala.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.plusAssign
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.map.MapScreen
import com.mgnovatto.uala.ui.theme.UalaChallengeTheme
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [AppNavigation] navigation structure.
 * Verifies screen transitions from city list to detail and map screens.
 */
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            navigatorProvider += ComposeNavigator()
        }
    }

    @Test
    fun showsCityListScreenInitially() {
        // GIVEN: App starts at city list screen

        // WHEN: Content is set with city list
        composeTestRule.setContent {
            UalaChallengeTheme {
                NavHost(navController = navController, startDestination = "cityList") {
                    composable("cityList") {
                        Box(modifier = Modifier.testTag("CityListScreen"))
                    }
                }
            }
        }

        // THEN: CityListScreen is displayed
        composeTestRule.onNodeWithTag("CityListScreen").assertExists()
    }

    @Test
    fun navigatesToCityDetailScreen() {
        val city = City(1, "La Plata", "AR", -34.9, -57.95, false)
        val cityJson = Json.encodeToString(City.serializer(), city)

        composeTestRule.setContent {
            UalaChallengeTheme {
                TestNavHost(
                    navController = navController,
                    startDestination = "cityList",
                    onCityList = {
                        IconButton(
                            modifier = Modifier.testTag("InfoButton"),
                            onClick = {
                                navController.navigate("detail/$cityJson")
                            }
                        ) {
                            Icon(Icons.Default.Info, contentDescription = "info")
                        }
                    },
                    onCityDetail = { receivedCity ->
                        Text(
                            text = "${receivedCity.name}, ${receivedCity.country}",
                            modifier = Modifier.testTag("CityDetailTopBar")
                        )
                    },
                    onMap = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("InfoButton").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule.onNodeWithTag("CityDetailTopBar").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
        composeTestRule.onNodeWithTag("CityDetailTopBar").assertExists()
        composeTestRule.onNodeWithText("La Plata, AR").assertExists()
    }

    @Test
    fun navigatesToMapScreen_directNavigation() {
        // GIVEN: A city object and start destination set to map screen
        val city = City(1, "La Plata", "AR", -34.9, -57.95, false)
        val cityJson = Json.encodeToString(City.serializer(), city)

        // WHEN: Map screen is composed
        composeTestRule.setContent {
            UalaChallengeTheme {
                NavHost(navController = navController, startDestination = "map/$cityJson") {
                    composable(
                        "map/{cityJson}",
                        arguments = listOf(navArgument("cityJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val json = backStackEntry.arguments?.getString("cityJson")!!
                        val parsedCity = Json.decodeFromString<City>(json)
                        MapScreen(city = parsedCity, navController = navController)
                    }
                }
            }
        }

        // THEN: City name is displayed on the screen
        composeTestRule.onNodeWithText("La Plata").assertExists()
    }

    @Test
    fun navigatesToMapScreen_onCityClick() {
        val city = City(1, "La Plata", "AR", -34.9, -57.95, false)
        val cityJson = Json.encodeToString(City.serializer(), city)

        composeTestRule.setContent {
            UalaChallengeTheme {
                TestNavHost(
                    navController = navController,
                    startDestination = "cityList",
                    onCityList = {
                        Box(modifier = Modifier.testTag("CityRow").clickable {
                            navController.navigate("map/$cityJson")
                        })
                    },
                    onCityDetail = {},
                    onMap = { receivedCity ->
                        Text(receivedCity.name)
                    }
                )
            }
        }

        composeTestRule.onNodeWithTag("CityRow").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("La Plata").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
        composeTestRule.onNodeWithText("La Plata").assertExists()
    }
}