package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import br.com.warehouse.shortenlinks.features.shorten.data.model.Links
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository

class FakeShortenRepository : ShortenRepository {
    private var shouldReturnError = false
    private var counter = 0

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun shortenUrl(url: String): ShortenResponse {
        if (shouldReturnError || url.isBlank()) {
            throw Exception("Invalid URL")
        }
        counter++
        return ShortenResponse(
            alias = "fakeAlias$counter",
            links = Links(
                self = url,
                shortUrl = "http://short.url/fakeAlias$counter"
            )
        )
    }

    override suspend fun getOriginalUrl(alias: String): GetUrlResponse {
        return GetUrlResponse(url = "http://long.url")
    }
}