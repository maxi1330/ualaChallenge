package com.mgnovatto.uala.data.remote.mappers

import com.mgnovatto.uala.data.remote.dto.CityDto
import com.mgnovatto.uala.data.remote.dto.CoordinatesDto
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the mapper function that converts a CityDto (network model)
 * to a CityEntity (database model).
 */
class CityRemoteMapperTest {

    @Test
    fun `given a CityDto, when toEntity is called, then it should map all properties correctly`() {
        // Given: A sample CityDto with standard values.
        val cityDto = CityDto(
            id = 1,
            name = "Buenos Aires",
            country = "AR",
            coordinates = CoordinatesDto(lat = -34.61315, lon = -58.37723)
        )

        // When: The toEntity extension function is called.
        val cityEntity = cityDto.toEntity()

        // Then: Verify that all properties of the resulting CityEntity match the source DTO.
        assertEquals(cityDto.id, cityEntity.id)
        assertEquals(cityDto.name, cityEntity.name)
        assertEquals(cityDto.country, cityEntity.country)
        assertEquals(cityDto.coordinates.lat, cityEntity.lat, 0.0)
        assertEquals(cityDto.coordinates.lon, cityEntity.lon, 0.0)
        // Verify that the default value for isFavorite is correctly set to false.
        assertEquals(false, cityEntity.isFavorite)
    }

    @Test
    fun `given a CityDto with zero coordinates, when toEntity is called, then it maps zero values correctly`() {
        // Given: A CityDto with zero values for latitude and longitude.
        val cityDtoWithZeros = CityDto(
            id = 2,
            name = "Null test",
            country = "XX",
            coordinates = CoordinatesDto(lat = 0.0, lon = 0.0)
        )

        // When: The toEntity extension function is called.
        val cityEntity = cityDtoWithZeros.toEntity()

        // Then: Verify that the zero coordinate values are preserved.
        assertEquals(0.0, cityEntity.lat, 0.0)
        assertEquals(0.0, cityEntity.lon, 0.0)
    }

    @Test
    fun `given a CityDto with non-ASCII characters, when toEntity is called, then it maps them correctly`() {
        // Given: A CityDto with special characters in its name.
        val cityWithSpecialChars = CityDto(
            id = 3,
            name = "San Pablo",
            country = "BR",
            coordinates = CoordinatesDto(lat = -23.55052, lon = -46.633308)
        )

        // When: The toEntity extension function is called.
        val cityEntity = cityWithSpecialChars.toEntity()

        // Then: Verify that the name with special characters is preserved.
        assertEquals("San Pablo", cityEntity.name)
        assertEquals(cityWithSpecialChars.id, cityEntity.id)
    }
}
