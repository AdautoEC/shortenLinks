package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.model.GetUrlResponse
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenResponse
import br.com.warehouse.shortenlinks.features.shorten.data.service.ShortenService
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import javax.inject.Inject

class ShortenRepositoryImpl @Inject constructor(
    private val service: ShortenService
) : ShortenRepository {

    override suspend fun shortenUrl(url: String): ShortenResponse {
        return service.shortenUrl(ShortenRequest(url))
    }

    override suspend fun getOriginalUrl(alias: String): GetUrlResponse {
        return service.getOriginalUrl(alias)
    }
}