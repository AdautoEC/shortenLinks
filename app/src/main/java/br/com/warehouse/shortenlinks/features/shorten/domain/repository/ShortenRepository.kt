package br.com.warehouse.shortenlinks.features.shorten.domain.repository

import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

/**
 * Interface for the shorten and get original URL operations.
 */
interface ShortenRepository {
    /**
     * Shortens a URL.
     *
     * @param url The URL to be shortened.
     * @return The [Shorten] domain model.
     */
    suspend fun shortenUrl(url: String): Shorten

    /**
     * Gets the original URL from a shortened one.
     *
     * @param alias The alias of the shortened URL.
     * @return The [OriginalUrl] domain model.
     */
    suspend fun getOriginalUrl(alias: String): OriginalUrl
}