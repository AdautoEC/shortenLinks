package br.com.warehouse.shortenlinks.features.shorten.data.mapper

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

fun ShortenResponse.toDomain(): Shorten {
    return Shorten(
        alias = alias,
        links = Links(
            self = links.self,
            shortUrl = links.shortUrl
        )
    )
}

fun GetUrlResponse.toDomain(): OriginalUrl {
    return OriginalUrl(url = url)
}