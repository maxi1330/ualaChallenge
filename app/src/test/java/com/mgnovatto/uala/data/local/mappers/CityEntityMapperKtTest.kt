package com.mgnovatto.uala.data.local.mappers

import com.mgnovatto.uala.data.local.db.CityEntity
import com.mgnovatto.uala.domain.model.City
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the mapper functions that convert between CityEntity and City domain models.
 */
class CityEntityMapperTest {

    @Test
    fun `given a CityEntity, when toDomain is called, then it should map all properties correctly`() {
        // Given: A sample CityEntity object with standard values.
        val cityEntity = CityEntity(
            id = 1,
            name = "Buenos Aires",
            country = "AR",
            lat = -34.61315,
            lon = -58.37723,
            isFavorite = true
        )

        // When: The toDomain extension function is called.
        val cityDomain = cityEntity.toDomain()

        // Then: Verify that all properties of the resulting City object match the source.
        assertEquals(cityEntity.id, cityDomain.id)
        assertEquals(cityEntity.name, cityDomain.name)
        assertEquals(cityEntity.country, cityDomain.country)
        assertEquals(cityEntity.lat, cityDomain.lat, 0.0)
        assertEquals(cityEntity.lon, cityDomain.lon, 0.0)
        assertEquals(cityEntity.isFavorite, cityDomain.isFavorite)
    }

    @Test
    fun `given a City domain model, when toEntity is called, then it should map all properties correctly`() {
        // Given: A sample City domain object with standard values.
        val cityDomain = City(
            id = 2,
            name = "London",
            country = "GB",
            lat = 51.50853,
            lon = -0.12574,
            isFavorite = false
        )

        // When: The toEntity extension function is called.
        val cityEntity = cityDomain.toEntity()

        // Then: Verify that all properties of the resulting CityEntity object match the source.
        assertEquals(cityDomain.id, cityEntity.id)
        assertEquals(cityDomain.name, cityEntity.name)
        assertEquals(cityDomain.country, cityEntity.country)
        assertEquals(cityDomain.lat, cityEntity.lat, 0.0)
        assertEquals(cityDomain.lon, cityEntity.lon, 0.0)
        assertEquals(cityDomain.isFavorite, cityEntity.isFavorite)
    }

    @Test
    fun `given a CityEntity with zero coordinates, when toDomain is called, then it maps zero values correctly`() {
        // Given: A CityEntity with zero values for latitude and longitude.
        val cityEntityWithZeros = CityEntity(
            id = 3,
            name = "Null test",
            country = "XX",
            lat = 0.0,
            lon = 0.0,
            isFavorite = false
        )

        // When: The toDomain extension function is called.
        val cityDomain = cityEntityWithZeros.toDomain()

        // Then: Verify that the zero values are preserved.
        assertEquals(0.0, cityDomain.lat, 0.0)
        assertEquals(0.0, cityDomain.lon, 0.0)
    }

    @Test
    fun `given a City domain model with non-ASCII characters, when toEntity is called, then it maps them correctly`() {
        // Given: A City domain object with special characters in its name.
        val cityWithSpecialChars = City(
            id = 4,
            name = "San Pablo",
            country = "BR",
            lat = -23.55052,
            lon = -46.633308,
            isFavorite = true
        )

        // When: The toEntity extension function is called.
        val cityEntity = cityWithSpecialChars.toEntity()

        // Then: Verify that the name with special characters is preserved.
        assertEquals("San Pablo", cityEntity.name)
        assertEquals(cityWithSpecialChars.id, cityEntity.id)
    }
}
