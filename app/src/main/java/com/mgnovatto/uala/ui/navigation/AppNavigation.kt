package com.mgnovatto.uala.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityDetail.CityDetailScreen
import com.mgnovatto.uala.ui.screens.cityList.CityListScreen
import com.mgnovatto.uala.ui.screens.map.MapScreen
import kotlinx.serialization.json.Json

/**
 * Sets up the main navigation graph for the app using Jetpack Compose Navigation.
 *
 * This function defines the navigation routes and destinations within the app:
 * - "cityList": Displays the list of cities.
 * - "detail/{cityJson}": Displays the detail screen for a selected city. The city is passed as a JSON-encoded argument.
 * - "map/{cityJson}": Displays the map screen for a selected city. The city is passed as a JSON-encoded argument.
 *
 * This navigation graph is used by the root composable to manage screen transitions.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cityList"
    ) {
        composable("cityList") {
            CityListScreen(navController = navController)
        }

        composable(
            route = "detail/{cityJson}",
            arguments = listOf(navArgument("cityJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityJson = backStackEntry.arguments?.getString("cityJson")
            cityJson?.let {
                val city = Json.decodeFromString<City>(it)
                CityDetailScreen(city = city, navController = navController)
            }
        }

        composable(
            route = "map/{cityJson}",
            arguments = listOf(navArgument("cityJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityJson = backStackEntry.arguments?.getString("cityJson")
            cityJson?.let {
                val city = Json.decodeFromString<City>(it)
                MapScreen(city = city, navController = navController)
            }
        }
    }
}