package com.mgnovatto.uala.data.remote

import com.mgnovatto.uala.data.remote.dto.CityDto
import com.mgnovatto.uala.data.remote.dto.WikipediaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the Retrofit API endpoints used to interact with remote services.
 *
 * This interface provides methods to:
 * - Download a list of cities from a static JSON file.
 * - Fetch a Wikipedia extract (introductory summary) for a given city.
 */
interface ApiService {
    /**
     * Downloads a list of cities from a predefined raw JSON endpoint hosted on GitHub.
     *
     * @return A list of [CityDto] representing the available cities.
     */
    @GET("hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json")
    suspend fun downloadCities(): List<CityDto>

    /**
     * Retrieves a plain text extract (introductory summary) from Wikipedia for the given title.
     *
     * @param action The type of API action (defaults to "query").
     * @param prop The properties to retrieve (defaults to "extracts").
     * @param exintro Whether to return only the introduction section (defaults to true).
     * @param explaintext Whether to return plain text without HTML (defaults to true).
     * @param format The format of the response (defaults to "json").
     * @param redirects Whether to follow redirects (1 = yes).
     * @param titles The title of the Wikipedia page to query.
     * @return A [WikipediaResponseDto] containing the extract.
     */
    @GET("api.php")
    suspend fun getWikipediaExtract(
        @Query("action") action: String = "query",
        @Query("prop") prop: String = "extracts",
        @Query("exintro") exintro: Boolean = true,
        @Query("explaintext") explaintext: Boolean = true,
        @Query("format") format: String = "json",
        @Query("redirects") redirects: Int = 1,
        @Query("titles") titles: String
    ): WikipediaResponseDto
}