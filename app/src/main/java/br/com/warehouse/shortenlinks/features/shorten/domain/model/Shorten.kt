package br.com.warehouse.shortenlinks.features.shorten.domain.model

/**
 * Represents the shortened URL in the domain layer.
 *
 * @property alias The alias of the shortened URL.
 * @property links The links associated with the shortened URL.
 */
data class Shorten(
    val alias: String,
    val links: Links
)

/**
 * Represents the links associated with the shortened URL.
 *
 * @property self The original URL.
 * @property shortUrl The shortened URL.
 */
data class Links(
    val self: String,
    val shortUrl: String
)
