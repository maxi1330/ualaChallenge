package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapComposable(
    modifier: Modifier = Modifier,
    cityName: String?,
    lat: Double?,
    lon: Double?
) {
    val defaultLocation = LatLng(-34.60, -58.38)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    val markerState = remember { MarkerState(position = defaultLocation) }

    LaunchedEffect(lat, lon) {
        if (lat != null && lon != null) {
            val newPosition = LatLng(lat, lon)
            cameraPositionState.animate(
                update = com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(newPosition, 12f),
                durationMs = 1000
            )
            markerState.position = newPosition
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        if (lat != null && lon != null) {
            Marker(
                state = markerState,
                title = cityName ?: "Ubicación Seleccionada"
            )
        }
    }
}