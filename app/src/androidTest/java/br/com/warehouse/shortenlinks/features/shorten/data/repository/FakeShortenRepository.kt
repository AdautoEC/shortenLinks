package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository

class FakeShortenRepository : ShortenRepository {
    private var shouldReturnError = false
    private var counter = 0

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun shortenUrl(url: String): Shorten {
        if (shouldReturnError || url.isBlank()) {
            throw Exception("Invalid URL")
        }
        counter++
        return Shorten(
            alias = "fakeAlias$counter",
            links = Links(
                self = url,
                shortUrl = "http://short.url/fakeAlias$counter"
            )
        )
    }

    override suspend fun getOriginalUrl(alias: String): OriginalUrl {
        if (shouldReturnError) {
            throw Exception("Invalid alias")
        }
        return OriginalUrl(url = "http://long.url/$alias")
    }
}
