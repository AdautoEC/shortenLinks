package br.com.warehouse.shortenlinks.features.shorten.presentation

import br.com.warehouse.shortenlinks.MainDispatcherRule
import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.core.validation.UrlValidator
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.GetOriginalUrlUseCase
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class ShortenViewModelTest {

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    private val shortenUrlUseCase: ShortenUrlUseCase = mockk()
    private val getOriginalUrlUseCase: GetOriginalUrlUseCase = mockk()
    private val urlValidator: UrlValidator = mockk()

    private lateinit var viewModel: ShortenViewModel

    @Before
    fun setUp() {
        every { urlValidator.isValid(any()) } returns true
        every { urlValidator.isValid("invalid url") } returns false
        viewModel = ShortenViewModel(shortenUrlUseCase, getOriginalUrlUseCase, urlValidator)
    }

    @Test
    fun `onEvent OnUrlChange should update url`() = runTest {
        val url = "https://google.com"
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        Assert.assertEquals(url, viewModel.uiState.value.url)
    }

    @Test
    fun `onEvent OnClearClick should clear url`() = runTest {
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange("some url"))
        viewModel.onEvent(ShortenScreenEvent.OnClearClick)
        Assert.assertEquals("", viewModel.uiState.value.url)
    }

    @Test
    fun `onEvent OnShortenClick with invalid url should set urlError`() = runTest {
        val invalidUrl = "invalid url"
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(invalidUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)
        Assert.assertEquals("Invalid URL", viewModel.uiState.value.urlError)
    }

    @Test
    fun `onEvent OnShortenClick with valid url should show loading then success`() = runTest {
        val url = "https://google.com"
        val shorten = Shorten(alias = "12345", links = Links(self = url, shortUrl = "http://short.url/12345"))
        coEvery { shortenUrlUseCase(url) } returns flow {
            emit(LoadingEvent.Loading)
            emit(LoadingEvent.Success(shorten))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertNull(viewModel.uiState.value.urlError)
        Assert.assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Success)
        Assert.assertEquals(shorten, (viewModel.uiState.value.shortenUrlState as SubmitLoadingState.Success).data)
        Assert.assertEquals(1, viewModel.uiState.value.history.size)
        Assert.assertEquals(shorten, viewModel.uiState.value.history.first())
    }

    @Test
    fun `onEvent OnShortenClick with valid url should show loading then error`() = runTest {
        val url = "https://google.com"
        val exception = Exception("Error")
        coEvery { shortenUrlUseCase(url) } returns flow {
            emit(LoadingEvent.Loading)
            emit(LoadingEvent.Error(exception))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertNull(viewModel.uiState.value.urlError)
        Assert.assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Error)
        Assert.assertEquals(exception, (viewModel.uiState.value.shortenUrlState as SubmitLoadingState.Error).exception)
    }

    @Test
    fun `onEvent OnShortenClick should not add duplicate to history`() = runTest {
        val url = "https://google.com"
        val shorten = Shorten(alias = "12345", links = Links(self = url, shortUrl = "http://short.url/12345"))
        coEvery { shortenUrlUseCase(url) } returns flow {
            emit(LoadingEvent.Success(shorten))
        }

        // First time
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        // Second time
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertEquals(1, viewModel.uiState.value.history.size)
    }

    @Test
    fun `onEvent OnShortenClick with short url should call getOriginalUrlUseCase`() = runTest {
        val alias = "12345"
        val shortUrl = "https://url-shortener-server.onrender.com/api/alias/$alias"
        coEvery { getOriginalUrlUseCase(alias) } returns flow {
            emit(LoadingEvent.Success(OriginalUrl("https://google.com")))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(shortUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

    }

    @Test
    fun `onEvent OnShortenClick with short url should show loading then success`() = runTest {
        val alias = "12345"
        val originalUrl = "https://google.com"
        val shortUrl = "https://url-shortener-server.onrender.com/api/alias/$alias"
        val expectedShorten = Shorten(alias = alias, links = Links(self = originalUrl, shortUrl = shortUrl))

        coEvery { getOriginalUrlUseCase(alias) } returns flow {
            emit(LoadingEvent.Loading)
            emit(LoadingEvent.Success(OriginalUrl(originalUrl)))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(shortUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Success)
        Assert.assertEquals(expectedShorten, (viewModel.uiState.value.shortenUrlState as SubmitLoadingState.Success).data)
        Assert.assertEquals(1, viewModel.uiState.value.history.size)
        Assert.assertEquals(expectedShorten, viewModel.uiState.value.history.first())
    }

    @Test
    fun `onEvent OnShortenClick with short url should show loading then 404 error`() = runTest {
        val alias = "12345"
        val shortUrl = "https://url-shortener-server.onrender.com/api/alias/$alias"
        val exception = HttpException(Response.error<Any>(404, "not found".toResponseBody()))

        coEvery { getOriginalUrlUseCase(alias) } returns flow {
            emit(LoadingEvent.Loading)
            emit(LoadingEvent.Error(exception))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(shortUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Error)
        Assert.assertEquals("Shortened URL not found.", viewModel.uiState.value.networkError)
    }

    @Test
    fun `onEvent OnShortenClick with short url should show loading then generic error`() = runTest {
        val alias = "12345"
        val shortUrl = "https://url-shortener-server.onrender.com/api/alias/$alias"
        val exception = Exception("generic error")

        coEvery { getOriginalUrlUseCase(alias) } returns flow {
            emit(LoadingEvent.Loading)
            emit(LoadingEvent.Error(exception))
        }

        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(shortUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)

        advanceUntilIdle()

        Assert.assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Error)
        Assert.assertEquals("generic error", viewModel.uiState.value.networkError)
    }
}