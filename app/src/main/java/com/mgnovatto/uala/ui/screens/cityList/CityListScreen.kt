package com.mgnovatto.uala.ui.screens.cityList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mgnovatto.uala.ui.screens.cityList.components.ErrorStateView
import com.mgnovatto.uala.ui.screens.cityList.components.LoadingStateView
import com.mgnovatto.uala.ui.screens.cityList.components.ReadyStateView

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
                message = (downloadState as DownloadState.Error).message,
                onRetry = { viewModel.retry() }
            )
        }

        is DownloadState.Ready -> {
            ReadyStateView(navController = navController)
        }
    }
}