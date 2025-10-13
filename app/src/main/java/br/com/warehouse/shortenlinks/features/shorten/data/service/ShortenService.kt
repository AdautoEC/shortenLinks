package br.com.warehouse.shortenlinks.features.shorten.data.service

import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ShortenService {

    @POST("shorten")
    suspend fun shortenUrl(@Body request: ShortenRequest): ShortenResponse
}