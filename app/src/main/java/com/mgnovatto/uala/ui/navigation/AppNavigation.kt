package com.mgnovatto.uala.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mgnovatto.uala.ui.screens.cityList.CityListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cityList"
    ) {
        composable("cityList") {
            CityListScreen()
        }
    }
}