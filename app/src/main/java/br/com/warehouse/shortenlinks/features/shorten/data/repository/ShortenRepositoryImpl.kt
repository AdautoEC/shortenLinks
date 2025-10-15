package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.mapper.toDomain
import br.com.warehouse.shortenlinks.features.shorten.data.model.ShortenRequest
import br.com.warehouse.shortenlinks.features.shorten.data.service.ShortenService
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import javax.inject.Inject

class ShortenRepositoryImpl @Inject constructor(
    private val service: ShortenService
) : ShortenRepository {

    override suspend fun shortenUrl(url: String): Shorten {
        return service.shortenUrl(ShortenRequest(url)).toDomain()
    }

    override suspend fun getOriginalUrl(alias: String): OriginalUrl {
        return service.getOriginalUrl(alias).toDomain()
    }
}