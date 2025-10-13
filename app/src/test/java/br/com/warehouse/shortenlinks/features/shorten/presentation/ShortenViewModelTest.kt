package br.com.warehouse.shortenlinks.features.shorten.presentation

import android.util.Patterns
import br.com.warehouse.shortenlinks.MainDispatcherRule
import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.data.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ShortenViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @Inject
    lateinit var shortenUrlUseCase: ShortenUrlUseCase

    private lateinit var viewModel: ShortenViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        mockkStatic(Patterns::class)
        every { Patterns.WEB_URL.matcher(any()).matches() } returns true
        every { Patterns.WEB_URL.matcher("invalid url").matches() } returns false
        viewModel = ShortenViewModel(shortenUrlUseCase)
    }

    @Test
    fun `onEvent OnUrlChange should update url`() = runTest {
        val url = "https://google.com"
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(url))
        assertEquals(url, viewModel.uiState.value.url)
    }

    @Test
    fun `onEvent OnClearClick should clear url`() = runTest {
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange("some url"))
        viewModel.onEvent(ShortenScreenEvent.OnClearClick)
        assertEquals("", viewModel.uiState.value.url)
    }

    @Test
    fun `onEvent OnShortenClick with invalid url should set urlError`() = runTest {
        val invalidUrl = "invalid url"
        viewModel.onEvent(ShortenScreenEvent.OnUrlChange(invalidUrl))
        viewModel.onEvent(ShortenScreenEvent.OnShortenClick)
        assertEquals("Invalid URL", viewModel.uiState.value.urlError)
        coVerify(exactly = 0) { shortenUrlUseCase(any()) }
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

        assertNull(viewModel.uiState.value.urlError)
        assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Success)
        assertEquals(shorten, (viewModel.uiState.value.shortenUrlState as SubmitLoadingState.Success).data)
        assertEquals(1, viewModel.uiState.value.history.size)
        assertEquals(shorten, viewModel.uiState.value.history.first())
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

        assertNull(viewModel.uiState.value.urlError)
        assertTrue(viewModel.uiState.value.shortenUrlState is SubmitLoadingState.Error)
        assertEquals(exception, (viewModel.uiState.value.shortenUrlState as SubmitLoadingState.Error).exception)
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

        assertEquals(1, viewModel.uiState.value.history.size)
    }
}
