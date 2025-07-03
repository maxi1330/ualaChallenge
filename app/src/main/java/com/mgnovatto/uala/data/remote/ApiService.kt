package com.mgnovatto.uala.data.remote

import com.mgnovatto.uala.data.remote.dto.CityDto
import com.mgnovatto.uala.data.remote.dto.WikipediaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json")
    suspend fun downloadCities(): List<CityDto>

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