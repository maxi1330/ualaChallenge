package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.mgnovatto.uala.domain.model.City

/**
 * UI component that displays a list of cities with a search bar and filter,
 * adapting its layout based on device orientation (portrait or landscape).
 *
 * In landscape mode, it splits the screen between the city list and a map view.
 * In portrait mode, it shows only the list.
 *
 * @param modifier Modifier applied to the root layout.
 * @param cities Paginated list of cities to display.
 * @param searchText Current value of the search input.
 * @param favoritesOnly Whether to filter only favorite cities.
 * @param onSearchTextChange Callback when search text changes.
 * @param onFavoritesOnlyChange Callback when the favorite-only toggle changes.
 * @param onToggleFavorite Callback when a city is marked/unmarked as favorite.
 * @param onCityClick Callback when a city row is clicked.
 * @param onInfoClick Callback when the info button for a city is clicked.
 */
@Composable
fun ReadyStateView(
    modifier: Modifier = Modifier,
    cities: LazyPagingItems<City>,
    searchText: String,
    favoritesOnly: Boolean,
    onSearchTextChange: (String) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onToggleFavorite: (City) -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    Scaffold(
        modifier = modifier.testTag("ReadyStateView")
    ) { paddingValues ->

        var selectedCity by remember { mutableStateOf<City?>(null) }

        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val isLandscape = maxWidth > 600.dp

            if (isLandscape && selectedCity == null && cities.itemCount > 0) {
                val firstCity = cities.peek(0)
                if (firstCity != null) {
                    selectedCity = firstCity
                }
            }

            if (isLandscape) {
                Row(Modifier.fillMaxSize()) {
                    CityListColumn(
                        modifier = Modifier.weight(0.4f),
                        cities = cities,
                        searchText = searchText,
                        onSearchTextChange = onSearchTextChange,
                        favoritesOnly = favoritesOnly,
                        onFavoritesOnlyChange = onFavoritesOnlyChange,
                        onToggleFavorite = onToggleFavorite,
                        onCityClick = { city ->
                            selectedCity = city
                        },
                        onInfoClick = onInfoClick
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
                    cities = cities,
                    searchText = searchText,
                    onSearchTextChange = onSearchTextChange,
                    favoritesOnly = favoritesOnly,
                    onFavoritesOnlyChange = onFavoritesOnlyChange,
                    onToggleFavorite = onToggleFavorite,
                    onCityClick = onCityClick,
                    onInfoClick = onInfoClick
                )
            }
        }
    }
}