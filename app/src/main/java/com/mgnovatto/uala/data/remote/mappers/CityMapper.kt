package com.mgnovatto.uala.data.remote.mappers

import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.data.remote.dto.CityDto

fun CityDto.toEntity(): CityEntity {
    return CityEntity(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.coordinates.lat,
        lon = this.coordinates.lon
    )
}