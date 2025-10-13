package br.com.warehouse.shortenlinks.features.shorten.data.service

import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import retrofit2.http.Body
import retrofit2.http.POST

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ShortenService {

    @POST("api/alias")
    suspend fun shortenUrl(@Body request: ShortenRequest): ShortenResponse

    @GET("api/alias/{alias}")
    suspend fun getOriginalUrl(@Path("alias") alias: String): GetUrlResponse
}