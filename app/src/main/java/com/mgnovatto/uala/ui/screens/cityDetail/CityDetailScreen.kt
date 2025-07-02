package com.mgnovatto.uala.ui.screens.cityDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.ui.screens.cityDetail.components.InfoRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
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
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow(
                    icon = Icons.Default.Info,
                    label = "ID",
                    value = city.id.toString()
                )
                InfoRow(
                    icon = Icons.Default.Place,
                    label = "Latitud",
                    value = "%.4f".format(city.lat)
                )
                InfoRow(
                    icon = Icons.Default.Place,
                    label = "Longitud",
                    value = "%.4f".format(city.lon)
                )
                InfoRow(
                    icon = Icons.Default.Favorite,
                    label = "Favorito",
                    value = if (city.isFavorite) "Sí" else "No"
                )
            }
        }
    }
}
