package com.mgnovatto.uala.ui.screens.map

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mgnovatto.uala.R
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityList.components.MapComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    city: City,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = city.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        MapComposable(
            modifier = Modifier.padding(paddingValues),
            cityName = city.name,
            lat = city.lat,
            lon = city.lon
        )
    }
}