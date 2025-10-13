package br.com.warehouse.shortenlinks.features.shorten.data.model

import com.google.gson.annotations.SerializedName

data class ShortenResponse(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("_links")
    val links: Links
)

data class Links(
    @SerializedName("self")
    val self: String,
    @SerializedName("short")
    val shortUrl: String
)
