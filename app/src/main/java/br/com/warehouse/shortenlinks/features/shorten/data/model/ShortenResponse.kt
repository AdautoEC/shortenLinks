package br.com.warehouse.shortenlinks.features.shorten.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the response from the API after shortening a URL.
 *
 * @property alias The alias of the shortened URL.
 * @property links The links provided in the response.
 */
data class ShortenResponse(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("_links")
    val links: Links
)

/**
 * Represents the links provided in the response.
 *
 * @property self The original URL.
 * @property shortUrl The shortened URL.
 */
data class Links(
    @SerializedName("self")
    val self: String,
    @SerializedName("short")
    val shortUrl: String
)
