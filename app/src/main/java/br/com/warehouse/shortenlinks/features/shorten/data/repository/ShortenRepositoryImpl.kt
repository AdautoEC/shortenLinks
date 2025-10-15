package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.mapper.toDomain
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.service.ShortenService
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import javax.inject.Inject

/**
 * Implementation of the [ShortenRepository] interface.
 *
 * This class is responsible for communicating with the [ShortenService] to perform
 * the shorten and get original URL operations.
 *
 * @param service The remote service for the shorten and get original URL operations.
 */
class ShortenRepositoryImpl @Inject constructor(
    private val service: ShortenService
) : ShortenRepository {

    /**
     * Shortens a URL.
     *
     * @param url The URL to be shortened.
     * @return The [Shorten] domain model.
     */
    override suspend fun shortenUrl(url: String): Shorten {
        return service.shortenUrl(ShortenRequest(url)).toDomain()
    }

    /**
     * Gets the original URL from a shortened one.
     *
     * @param alias The alias of the shortened URL.
     * @return The [OriginalUrl] domain model.
     */
    override suspend fun getOriginalUrl(alias: String): OriginalUrl {
        return service.getOriginalUrl(alias).toDomain()
    }
}