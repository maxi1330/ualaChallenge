package com.mgnovatto.uala.data.remote.mappers

import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.data.remote.dto.CityDto

/**
 * Maps a [CityDto] received from the remote API to a local [CityEntity] used by the database.
 */
fun CityDto.toEntity(): CityEntity {
    return CityEntity(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.coordinates.lat,
        lon = this.coordinates.lon
    )
}