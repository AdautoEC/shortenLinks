package br.com.warehouse.shortenlinks.features.shorten.data.mapper

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

/**
 * Maps a [ShortenResponse] from the data layer to a [Shorten] in the domain layer.
 */
fun ShortenResponse.toDomain(): Shorten {
    return Shorten(
        alias = alias,
        links = Links(
            self = links.self,
            shortUrl = links.shortUrl
        )
    )
}

/**
 * Maps a [GetUrlResponse] from the data layer to an [OriginalUrl] in the domain layer.
 */
fun GetUrlResponse.toDomain(): OriginalUrl {
    return OriginalUrl(url = url)
}