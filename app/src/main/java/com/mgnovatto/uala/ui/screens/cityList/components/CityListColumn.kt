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