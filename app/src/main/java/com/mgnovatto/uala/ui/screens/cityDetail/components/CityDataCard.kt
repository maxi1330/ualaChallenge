package com.mgnovatto.uala.ui.screens.cityDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mgnovatto.uala.R
import com.mgnovatto.uala.domain.model.City
import com.mgnovatto.uala.utils.getCountryNameFromCode

/**
 * Displays a card containing basic data about a given city, such as country name,
 * latitude, and longitude.
 *
 * @param city The city object containing geographic and country code information.
 */
@Composable
fun CityDataCard(city: City) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val countryName = remember(city.country) {
            getCountryNameFromCode(city.country)
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.title_detail_card),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Icons.Default.Home,
                label = stringResource(R.string.title_country_card),
                value = countryName
            )
            InfoRow(
                icon = Icons.Default.Place,
                label = stringResource(R.string.title_latitude_card),
                value = "%.4f".format(city.lat)
            )
            InfoRow(
                icon = Icons.Default.Place,
                label = stringResource(R.string.title_longitude_card),
                value = "%.4f".format(city.lon)
            )
        }
    }
}