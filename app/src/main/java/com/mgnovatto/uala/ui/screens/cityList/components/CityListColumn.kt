package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.mgnovatto.uala.domain.model.City

/**
 * Composable that displays a column containing a search bar with filters and a scrollable list of cities.
 *
 * This is the main UI for showing a paginated list of cities. It includes:
 * - A search bar and favorite filter toggle at the top.
 * - A loading indicator while cities are being fetched.
 * - A scrollable list of cities using Paging.
 * - Retry button in case of loading error.
 *
 * @param modifier Modifier applied to the entire column.
 * @param cities The paginated list of [City] items to display.
 * @param searchText The current value of the search input.
 * @param onSearchTextChange Callback when the search text changes.
 * @param favoritesOnly Whether to show only favorite cities.
 * @param onFavoritesOnlyChange Callback when the favorites-only toggle changes.
 * @param onToggleFavorite Callback when the favorite icon of a city is toggled.
 * @param onCityClick Callback when a city row is clicked.
 * @param onInfoClick Callback when the info icon for a city is clicked.
 */
@Composable
fun CityListColumn(
    modifier: Modifier = Modifier,
    cities: LazyPagingItems<City>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    favoritesOnly: Boolean,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onToggleFavorite: (City) -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    Column(modifier) {
        SearchBarWithFilter(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            favoritesOnly = favoritesOnly,
            onFavoritesOnlyChange = onFavoritesOnlyChange
        )
        if (cities.loadState.refresh is LoadState.Loading || cities.loadState.append is LoadState.Loading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(
                    count = cities.itemCount,
                    key = { index -> cities.peek(index)?.id ?: -1 }
                ) { index ->
                    val city = cities[index]
                    if (city != null) {
                        CityRow(
                            city = city,
                            onToggleFavorite = { onToggleFavorite(city) },
                            onRowClick = { onCityClick(city) },
                            onInfoClick = { onInfoClick(city) }
                        )
                        HorizontalDivider()
                    }
                }

                if (cities.loadState.append is LoadState.Error) {
                    item {
                        Button(
                            onClick = { cities.retry() },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Reintentar carga")
                        }
                    }
                }
            }
        }
    }
}