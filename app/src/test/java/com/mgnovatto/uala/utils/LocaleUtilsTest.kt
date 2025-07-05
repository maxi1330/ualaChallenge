package com.mgnovatto.uala.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the utility functions in LocaleUtils.kt.
 * Since the function relies on standard Java/Kotlin functionality,
 * we can test it directly without mocking the Android framework.
 */
class LocaleUtilsTest {

    @Test
    fun `given a valid country code, when getCountryNameFromCode is called, then it returns the full country name`() {
        // Given: A valid ISO country code.
        val countryCode = "AR"

        // When: The function is called.
        val result = getCountryNameFromCode(countryCode)

        // Then: The result should be the full, correct country name.
        assertEquals("Argentina", result)
    }

    @Test
    fun `given an invalid country code, when getCountryNameFromCode is called, then it returns the original code`() {
        // Given: An invalid or unknown country code.
        val invalidCode = "XX"

        // When: The function is called.
        val result = getCountryNameFromCode(invalidCode)

        // Then: The function should return the original invalid code as it cannot be resolved.
        assertEquals(invalidCode, result)
    }

    @Test
    fun `given an empty country code, when getCountryNameFromCode is called, then it returns an empty string`() {
        // Given: An empty string as the country code.
        val emptyCode = ""

        // When: The function is called.
        val result = getCountryNameFromCode(emptyCode)

        // Then: The result should be an empty string.
        assertEquals("", result)
    }
}
