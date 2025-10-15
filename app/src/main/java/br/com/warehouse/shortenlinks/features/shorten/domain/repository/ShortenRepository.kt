package br.com.warehouse.shortenlinks.features.shorten.domain.repository

import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

interface ShortenRepository {
    suspend fun shortenUrl(url: String): Shorten
    suspend fun getOriginalUrl(alias: String): OriginalUrl
}