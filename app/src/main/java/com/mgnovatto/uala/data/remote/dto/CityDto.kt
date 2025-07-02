package com.mgnovatto.uala.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    @SerialName("_id") val id: Int,
    val name: String,
    val country: String,
    @SerialName("coord") val coordinates: CoordinatesDto
)

@Serializable
data class CoordinatesDto(
    val lon: Double,
    val lat: Double
)