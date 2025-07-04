package com.mgnovatto.uala.ui.screens.cityDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mgnovatto.uala.R
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityDetail.components.CityDataCard
import com.mgnovatto.uala.ui.screens.cityDetail.components.CityDescriptionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    city: City,
    navController: NavController,
    viewModel: CityDetailViewModel = hiltViewModel()
) {
    val description by viewModel.description.collectAsStateWithLifecycle()

    val loadingText = stringResource(R.string.description_loading)
    val notFoundText = stringResource(R.string.description_not_found)

    LaunchedEffect(Unit) {
        viewModel.fetchCityDescription(
            city.name,
            city.country,
            loadingText,
            notFoundText
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${city.name}, ${city.country}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CityDataCard(city = city)
            }
            item {
                CityDescriptionCard(description = description)
            }
        }
    }
}