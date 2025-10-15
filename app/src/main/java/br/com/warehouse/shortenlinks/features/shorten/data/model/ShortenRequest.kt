package br.com.warehouse.shortenlinks.features.shorten.data.model

/**
 * Represents the request sent to the API to shorten a URL.
 *
 * @property url The URL to be shortened.
 */
data class ShortenRequest(val url: String)