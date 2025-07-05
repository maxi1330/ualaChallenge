package com.mgnovatto.uala.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a city received from the remote API.
 */
@Serializable
data class CityDto(
    @SerialName("_id") val id: Int,
    val name: String,
    val country: String,
    @SerialName("coord") val coordinates: CoordinatesDto
)

/**
 * DTO representing the geographical coordinates of a city.
 */
@Serializable
data class CoordinatesDto(
    val lon: Double,
    val lat: Double
)