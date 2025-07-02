package com.mgnovatto.uala.ui.screens.cityList

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
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityList.components.CityListColumn
import com.mgnovatto.uala.ui.screens.cityList.components.MapComposable

@Composable
fun CityListScreen() {
    var searchText by remember { mutableStateOf("") }
    var favoritesOnly by remember { mutableStateOf(false) }
    var cities by remember {
        mutableStateOf(
            listOf(
                City(707860, "La Plata", "AR", -34.60, -58.38, isFavorite = true),
                City(519188, "Auckland", "NZ", 37.666668, 55.683334, isFavorite = true),
                City(2643743, "London", "GB", -0.12574, 51.50853, isFavorite = false)
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
                    onToggleFavorite = { println("OnToggleFavorite") },
                    onCityClick = { city ->
                        selectedCity = city
                    }
                )
                MapComposable(
                    modifier = Modifier.weight(0.5f),
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
                    selectedCity = city
                }
            )
        }
    }
}
