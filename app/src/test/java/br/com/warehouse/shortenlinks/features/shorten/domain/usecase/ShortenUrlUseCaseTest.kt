package br.com.warehouse.shortenlinks.features.shorten.domain.usecase

import app.cash.turbine.test
import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ShortenUrlUseCaseTest {

    private val repository: ShortenRepository = mock()
    private val useCase = ShortenUrlUseCase(repository)

    @Test
    fun `given repository returns success, when use case is invoked, then emits Loading and Success`() = runTest {
        // Arrange
        val url = "https://google.com"
        val shorten = Shorten("alias", Links(url, "https://short.url"))
        whenever(repository.shortenUrl(url)).thenReturn(shorten)

        // Act & Assert
        useCase(url).test {
            assertEquals(LoadingEvent.Loading, awaitItem())
            assertEquals(LoadingEvent.Success(shorten), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `given repository throws exception, when use case is invoked, then emits Loading and Error`() = runTest {
        // Arrange
        val url = "https://google.com"
        val exception = RuntimeException("Test error")
        whenever(repository.shortenUrl(url)).thenThrow(exception)

        // Act & Assert
        useCase(url).test {
            assertEquals(LoadingEvent.Loading, awaitItem())
            assertEquals(LoadingEvent.Error(exception), awaitItem())
            awaitComplete()
        }
    }
}
