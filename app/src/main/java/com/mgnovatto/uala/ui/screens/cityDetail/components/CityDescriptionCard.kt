package com.mgnovatto.uala.ui.screens.cityDetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.mgnovatto.uala.R


/**
 * Displays a card containing a city's description.
 *
 * Shows a loading spinner if the description is still loading,
 * and a default message if no description is available.
 *
 * @param description The description of the city to display, or null if not available.
 */
@Composable
fun CityDescriptionCard(description: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.title_description_card),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (description == "Cargando descripción...") {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            } else {
                Text(
                    text = description ?: stringResource(R.string.description_not_found),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = if (description == null) FontStyle.Italic else FontStyle.Normal
                )
            }
        }
    }
}