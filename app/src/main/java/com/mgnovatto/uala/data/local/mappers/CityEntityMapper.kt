package com.mgnovatto.uala.data.local.mappers

import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.domain.model.City

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