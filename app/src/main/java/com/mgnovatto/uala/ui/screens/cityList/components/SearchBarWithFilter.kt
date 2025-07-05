package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable that displays a search bar with an optional filter toggle for favorite cities.
 *
 * @param modifier Modifier applied to the root row layout.
 * @param searchText Current value of the search input.
 * @param onSearchTextChange Callback triggered when the search input changes.
 * @param favoritesOnly Whether the "favorites only" filter is currently active.
 * @param onFavoritesOnlyChange Callback triggered when the filter toggle is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithFilter(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    favoritesOnly: Boolean,
    onFavoritesOnlyChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Buscar ciudad...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = RoundedCornerShape(32.dp),

            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text("Solo favoritos")
                }
            },
            state = rememberTooltipState()
        ) {
            IconButton(onClick = { onFavoritesOnlyChange(!favoritesOnly) }) {
                Icon(
                    imageVector = if (favoritesOnly) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Filtrar por favoritos",
                    tint = if (favoritesOnly) Color.Red else Color.Gray
                )
            }
        }
    }
}