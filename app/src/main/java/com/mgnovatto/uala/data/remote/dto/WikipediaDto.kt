package com.mgnovatto.uala.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WikipediaResponseDto(
    val query: WikipediaQueryDto? = null
)

@Serializable
data class WikipediaQueryDto(
    val pages: Map<String, WikipediaPageDto>? = null
)

@Serializable
data class WikipediaPageDto(
    val title: String,
    val extract: String? = null
)