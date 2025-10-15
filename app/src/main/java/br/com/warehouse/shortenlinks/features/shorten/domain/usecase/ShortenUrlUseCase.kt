package br.com.warehouse.shortenlinks.features.shorten.domain.usecase

import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for shortening a URL.
 *
 * @param repository The repository to be used for the shorten operation.
 */
class ShortenUrlUseCase @Inject constructor(
    private val repository: ShortenRepository
) {
    /**
     * Invokes the use case.
     *
     * @param url The URL to be shortened.
     * @return A [Flow] of [LoadingEvent]s representing the state of the operation.
     */
    operator fun invoke(url: String): Flow<LoadingEvent<Shorten>> = flow {
        emit(LoadingEvent.Loading)
        val result = repository.shortenUrl(url)
        emit(LoadingEvent.Success(result))
    }.catch { e ->
        emit(LoadingEvent.Error(e))
    }
}