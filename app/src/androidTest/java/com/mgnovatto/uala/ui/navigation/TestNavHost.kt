package com.mgnovatto.uala.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mgnovatto.uala.domain.model.City
import kotlinx.serialization.json.Json

@Composable
fun TestNavHost(
    navController: NavHostController,
    startDestination: String,
    onCityList: @Composable () -> Unit,
    onCityDetail: @Composable (City) -> Unit,
    onMap: @Composable (City) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("cityList") {
            onCityList()
        }
        composable(
            "detail/{cityJson}",
            arguments = listOf(navArgument("cityJson") { type = NavType.StringType })
        ) {
            val city = Json.decodeFromString<City>(it.arguments!!.getString("cityJson")!!)
            onCityDetail(city)
        }
        composable(
            "map/{cityJson}",
            arguments = listOf(navArgument("cityJson") { type = NavType.StringType })
        ) {
            val city = Json.decodeFromString<City>(it.arguments!!.getString("cityJson")!!)
            onMap(city)
        }
    }
}