package br.com.warehouse.shortenlinks.features.shorten.domain.repository

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse

interface ShortenRepository {
    suspend fun shortenUrl(url: String): ShortenResponse
    suspend fun getOriginalUrl(alias: String): GetUrlResponse
}