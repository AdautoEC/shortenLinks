package br.com.warehouse.shortenlinks.features.shorten.domain.usecase

import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOriginalUrlUseCase @Inject constructor(
    private val repository: ShortenRepository
) {
    operator fun invoke(alias: String): Flow<LoadingEvent<OriginalUrl>> = flow {
        emit(LoadingEvent.Loading)
        try {
            val result = repository.getOriginalUrl(alias)
            emit(LoadingEvent.Success(result))
        } catch (e: Exception) {
            emit(LoadingEvent.Error(e))
        }
    }
}
