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