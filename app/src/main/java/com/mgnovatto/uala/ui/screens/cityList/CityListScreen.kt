package com.mgnovatto.uala.ui.screens.cityList

import android.net.Uri
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityList.components.CityListColumn
import com.mgnovatto.uala.ui.screens.cityList.components.MapComposable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.json.Json

@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityListViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }
    var favoritesOnly by remember { mutableStateOf(false) }
    var cities by remember {
        mutableStateOf(
            listOf(
                City(707860, "Ciudad Autónoma de Buenos Aires", "AR", -58.450001, -34.599998, isFavorite = true),
                City(2193733, "Auckland", "NZ", 174.766663, -36.866669, isFavorite = true),
                City(2643743, "London", "GB", -0.12574, 51.50853, isFavorite = false),
                City(4542692, "Miami", "US", -80.533112, 25.61705, isFavorite = false)
            )
        )
    }
    var selectedCity by remember { mutableStateOf<City?>(null) }

    val filteredCities = cities.filter { city ->
        val matchesSearch = city.name.startsWith(searchText, ignoreCase = true)
        val matchesFavorite = !favoritesOnly || city.isFavorite
        matchesSearch && matchesFavorite
    }

    BoxWithConstraints {
        val isLandscape = maxWidth > 600.dp

        if (filteredCities.isNotEmpty() && selectedCity == null) {
            selectedCity = filteredCities.first()
        }

        if (isLandscape) {
            Row(Modifier.fillMaxSize()) {
                CityListColumn(
                    modifier = Modifier.weight(0.4f),
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    favoritesOnly = favoritesOnly,
                    onFavoritesOnlyChange = { favoritesOnly = it },
                    cities = filteredCities,
                    onToggleFavorite = { cityId ->
                        cities = cities.map { if (it.id == cityId) it.copy(isFavorite = !it.isFavorite) else it }
                    },
                    onCityClick = { city ->
                        selectedCity = city
                    },
                    onInfoClick = { city ->
                        val cityJson = Json.encodeToString(city)
                        navController.navigate("detail/${Uri.encode(cityJson)}")
                    }
                )
                MapComposable(
                    modifier = Modifier.weight(0.6f),
                    cityName = selectedCity?.name,
                    lat = selectedCity?.lat,
                    lon = selectedCity?.lon
                )
            }
        } else {
            CityListColumn(
                modifier = Modifier.fillMaxSize(),
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                favoritesOnly = favoritesOnly,
                onFavoritesOnlyChange = { favoritesOnly = it },
                cities = filteredCities,
                onToggleFavorite = { cityId ->
                    cities = cities.map { if (it.id == cityId) it.copy(isFavorite = !it.isFavorite) else it }
                },
                onCityClick = { city ->
                    val cityJson = Json.encodeToString(city)
                    navController.navigate("map/${Uri.encode(cityJson)}")
                },
                onInfoClick = { city ->
                    val cityJson = Json.encodeToString(city)
                    navController.navigate("detail/${Uri.encode(cityJson)}")
                }
            )
        }
    }
}