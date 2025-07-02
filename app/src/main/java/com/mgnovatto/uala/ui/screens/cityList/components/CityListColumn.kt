package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mgnovatto.uala.domain.model.City

@Composable
fun CityListColumn(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    favoritesOnly: Boolean,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    cities: List<City>,
    onToggleFavorite: (Int) -> Unit,
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
        LazyColumn {
            items(cities, key = { it.id }) { city ->
                CityRow(
                    city = city,
                    onToggleFavorite = { onToggleFavorite(city.id) },
                    onRowClick = { onCityClick(city) },
                    onInfoClick = { onInfoClick(city) }
                )
                HorizontalDivider()
            }
        }
    }
}