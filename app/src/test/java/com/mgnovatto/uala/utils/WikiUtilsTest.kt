package com.mgnovatto.uala.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * This class verifies the text processing and validation logic for Wikipedia extracts.
 */
class WikiUtilsTest {

    // isValidDescription

    @Test
    fun `given a valid description, when isValidDescription is called, then it returns true`() {
        // Given
        val validDescription = "London is the capital of England."

        // When
        val result = WikiUtils.isValidDescription(validDescription)

        // Then
        assertTrue(result)
    }

    @Test
    fun `given a null string, when isValidDescription is called, then it returns false`() {
        // Given
        val nullDescription = null

        // When
        val result = WikiUtils.isValidDescription(nullDescription)

        // Then
        assertFalse(result)
    }

    @Test
    fun `given a blank string, when isValidDescription is called, then it returns false`() {
        // Given
        val blankDescription = "   "

        // When
        val result = WikiUtils.isValidDescription(blankDescription)

        // Then
        assertFalse(result)
    }

    @Test
    fun `given an english disambiguation text, when isValidDescription is called, then it returns false`() {
        // Given
        val disambiguationText = "London may refer to: a city, a person..."

        // When
        val result = WikiUtils.isValidDescription(disambiguationText)

        // Then
        assertFalse(result)
    }

    @Test
    fun `given a spanish disambiguation text, when isValidDescription is called, then it returns false`() {
        // Given
        val disambiguationText = "Londres puede referirse a: una ciudad, una persona..."

        // When
        val result = WikiUtils.isValidDescription(disambiguationText)

        // Then
        assertFalse(result)
    }

    // cleanWikipediaExtract

    @Test
    fun `given a text with citations, when cleanWikipediaExtract is called, then it removes them`() {
        // Given
        val rawText = "This is a sentence [1] with some citations [23] inside."
        val expectedText = "This is a sentence with some citations inside."

        // When
        val result = WikiUtils.cleanWikipediaExtract(rawText)

        // Then
        // We replace multiple spaces with a single one to make the assertion robust.
        assertEquals(expectedText, result?.replace(Regex("\\s+"), " "))
    }

    @Test
    fun `given a text without citations, when cleanWikipediaExtract is called, then it returns the same text`() {
        // Given
        val cleanText = "This is a clean sentence."

        // When
        val result = WikiUtils.cleanWikipediaExtract(cleanText)

        // Then
        assertEquals(cleanText, result)
    }

    @Test
    fun `given a null string, when cleanWikipediaExtract is called, then it returns null`() {
        // Given
        val nullText = null

        // When
        val result = WikiUtils.cleanWikipediaExtract(nullText)

        // Then
        assertNull(result)
    }

    @Test
    fun `given a text with leading and trailing spaces, when cleanWikipediaExtract is called, then it trims them`() {
        // Given
        val textWithSpaces = "  A text with spaces.  "
        val expectedText = "A text with spaces."

        // When
        val result = WikiUtils.cleanWikipediaExtract(textWithSpaces)

        // Then
        assertEquals(expectedText, result)
    }
}
