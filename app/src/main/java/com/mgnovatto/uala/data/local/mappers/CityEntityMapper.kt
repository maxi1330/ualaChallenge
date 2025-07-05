package com.mgnovatto.uala.data.local.mappers

import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.domain.model.City

/**
 * Converts a [CityEntity] (local database model) to a [City] (domain model).
 *
 * This function is used to map data from the Room database representation
 * to the domain layer model used by the app's business logic.
 *
 * @return A [City] object containing the same values as the original [CityEntity].
 */
fun CityEntity.toDomain(): City {
    return City(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.lat,
        lon = this.lon,
        isFavorite = this.isFavorite
    )
}

/**
 * Converts a [City] (domain model) to a [CityEntity] (local database model).
 *
 * This function is used to store domain-level data into the Room database
 * by transforming it into a persistable format.
 *
 * @return A [CityEntity] object containing the same values as the original [City].
 */
fun City.toEntity(): CityEntity {
    return CityEntity(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.lat,
        lon = this.lon,
        isFavorite = this.isFavorite
    )
}