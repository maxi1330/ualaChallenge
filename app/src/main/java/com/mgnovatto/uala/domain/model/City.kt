package com.mgnovatto.uala.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val lon: Double,
    val lat: Double,
    val isFavorite: Boolean
)