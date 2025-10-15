package br.com.warehouse.shortenlinks.features.shorten.domain.model

data class Shorten(
    val alias: String,
    val links: Links
)

data class Links(
    val self: String,
    val shortUrl: String
)
