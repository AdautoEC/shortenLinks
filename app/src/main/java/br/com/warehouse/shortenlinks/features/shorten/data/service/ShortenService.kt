package br.com.warehouse.shortenlinks.features.shorten.data.service

import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import retrofit2.http.Body
import retrofit2.http.POST

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for the shorten API.
 */
interface ShortenService {

    /**
     * Shortens a URL.
     *
     * @param request The request body containing the URL to be shortened.
     * @return The response containing the shortened URL information.
     */
    @POST("api/alias")
    suspend fun shortenUrl(@Body request: ShortenRequest): ShortenResponse

    /**
     * Gets the original URL from a shortened one.
     *
     * @param alias The alias of the shortened URL.
     * @return The response containing the original URL.
     */
    @GET("api/alias/{alias}")
    suspend fun getOriginalUrl(@Path("alias") alias: String): GetUrlResponse
}