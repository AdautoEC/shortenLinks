package br.com.warehouse.shortenlinks.features.shorten.domain.usecase

import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for getting the original URL from a shortened one.
 *
 * @param repository The repository to be used for the get original URL operation.
 */
class GetOriginalUrlUseCase @Inject constructor(
    private val repository: ShortenRepository
) {
    /**
     * Invokes the use case.
     *
     * @param alias The alias of the shortened URL.
     * @return A [Flow] of [LoadingEvent]s representing the state of the operation.
     */
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
