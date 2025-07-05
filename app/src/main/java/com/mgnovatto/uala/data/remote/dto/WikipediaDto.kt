package com.mgnovatto.uala.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Represents the top-level response object returned by the Wikipedia API.
 */
@Serializable
data class WikipediaResponseDto(
    val query: WikipediaQueryDto? = null
)

/**
 * Represents the query section of the Wikipedia API response.
 */
@Serializable
data class WikipediaQueryDto(
    val pages: Map<String, WikipediaPageDto>? = null
)

/**
 * Represents an individual Wikipedia page returned in the response.
 */
@Serializable
data class WikipediaPageDto(
    val title: String,
    val extract: String? = null
)