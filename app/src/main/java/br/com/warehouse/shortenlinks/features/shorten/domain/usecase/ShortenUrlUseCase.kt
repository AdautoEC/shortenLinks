package br.com.warehouse.shortenlinks.features.shorten.domain.usecase

import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.features.shorten.data.repository.ShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShortenUrlUseCase @Inject constructor(
    private val repository: ShortenRepository
) {
    operator fun invoke(url: String): Flow<LoadingEvent<Shorten>> = flow {
        emit(LoadingEvent.Loading)
        val result = repository.shortenUrl(url)
        emit(LoadingEvent.Success(result))
    }.catch { e ->
        emit(LoadingEvent.Error(e))
    }
}