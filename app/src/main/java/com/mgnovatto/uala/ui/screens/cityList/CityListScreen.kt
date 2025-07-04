package com.mgnovatto.uala.ui.screens.cityList

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.mgnovatto.uala.R
import com.mgnovatto.uala.ui.screens.cityList.components.ErrorStateView
import com.mgnovatto.uala.ui.screens.cityList.components.LoadingStateView
import com.mgnovatto.uala.ui.screens.cityList.components.ReadyStateView
import kotlinx.serialization.json.Json

@Composable
fun CityListScreen(
    navController: NavController,
    viewModel: CityListViewModel = hiltViewModel()
) {
    val downloadState by viewModel.downloadState.collectAsStateWithLifecycle()

    when (downloadState) {
        is DownloadState.Loading -> {
            LoadingStateView(modifier = Modifier.fillMaxSize())
        }

        is DownloadState.Error -> {
            ErrorStateView(
                message = stringResource(R.string.download_error_message),
                onRetry = { viewModel.retry() }
            )
        }

        is DownloadState.Ready -> {
            val cities = viewModel.cities.collectAsLazyPagingItems()
            val searchText by viewModel.searchText.collectAsStateWithLifecycle()
            val favoritesOnly by viewModel.favoritesOnly.collectAsStateWithLifecycle()

            ReadyStateView(
                cities = cities,
                searchText = searchText,
                favoritesOnly = favoritesOnly,
                onSearchTextChange = viewModel::onSearchTextChange,
                onFavoritesOnlyChange = viewModel::onFavoritesOnlyChange,
                onToggleFavorite = viewModel::onToggleFavorite,
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