package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse

interface ShortenRepository {
    suspend fun shortenUrl(url: String): ShortenResponse
}