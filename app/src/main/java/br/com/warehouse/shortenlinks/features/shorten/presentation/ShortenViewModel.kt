package br.com.warehouse.shortenlinks.features.shorten.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.core.validation.UrlValidator
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.GetOriginalUrlUseCase
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * ViewModel for the [ShortenScreen].
 *
 * This ViewModel is responsible for handling the business logic of the screen,
 * processing user events, and managing the UI state.
 *
 * @param shortenUrlUseCase The use case for shortening a URL.
 * @param getOriginalUrlUseCase The use case for getting the original URL from a shortened one.
 * @param urlValidator The validator for the URL input.
 */
@HiltViewModel
class ShortenViewModel @Inject constructor(
    private val shortenUrlUseCase: ShortenUrlUseCase,
    private val getOriginalUrlUseCase: GetOriginalUrlUseCase,
    private val urlValidator: UrlValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShortenScreenState())
    val uiState: StateFlow<ShortenScreenState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * Handles events from the UI.
     *
     * @param event The event to be processed.
     */
    fun onEvent(event: ShortenScreenEvent) {
        when (event) {
            is ShortenScreenEvent.OnUrlChange -> {
                _uiState.update { it.copy(url = event.url) }
            }
            is ShortenScreenEvent.OnShortenClick -> {
                submitUrl()
            }
            is ShortenScreenEvent.OnClearClick -> {
                _uiState.update { it.copy(url = "") }
            }
            is ShortenScreenEvent.OnDismissErrorDialog -> {
                _uiState.update { it.copy(networkError = null, shortenUrlState = SubmitLoadingState.Idle) }
            }
        }
    }

    private fun submitUrl() {
        val url = _uiState.value.url
        if (url.startsWith(SHORT_URL_BASE)) {
            val alias = url.substringAfter(SHORT_URL_BASE)
            getOriginalUrl(alias)
        } else {
            shortenUrl(url)
        }
    }

    private fun getOriginalUrl(alias: String) {
        viewModelScope.launch {
            getOriginalUrlUseCase(alias).collect { event ->
                when (event) {
                    is LoadingEvent.Loading -> {
                        _uiState.update { it.copy(shortenUrlState = SubmitLoadingState.Loading) }
                    }
                    is LoadingEvent.Success -> {
                        val originalUrl = event.data.url
                        val result = Shorten(
                            alias = alias,
                            links = br.com.warehouse.shortenlinks.features.shorten.domain.model.Links(
                                self = originalUrl,
                                shortUrl = "$SHORT_URL_BASE$alias"
                            )
                        )
                        _uiState.update {
                            it.copy(
                                shortenUrlState = SubmitLoadingState.Success(result),
                                history = if (it.history.none { h -> h.alias == result.alias }) {
                                    listOf(result) + it.history
                                } else {
                                    it.history
                                },
                                url = ""
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowSnackbar("Original URL copied to clipboard!"))
                    }
                    is LoadingEvent.Error -> {
                        val errorMessage = when (event.exception) {
                            is UnknownHostException -> "Please check your internet connection."
                            is HttpException -> if (event.exception.code() == 404) "Shortened URL not found." else "An unexpected error occurred."
                            else -> event.exception.message ?: "An unknown error occurred"
                        }
                        _uiState.update {
                            it.copy(
                                shortenUrlState = SubmitLoadingState.Error(event.exception),
                                networkError = errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun shortenUrl(url: String) {
        if (urlValidator.isValid(url)) {
            _uiState.update { it.copy(urlError = null) }
            viewModelScope.launch {
                shortenUrlUseCase(url).collect { event ->
                    when (event) {
                        is LoadingEvent.Loading -> {
                            _uiState.update { it.copy(shortenUrlState = SubmitLoadingState.Loading) }
                        }
                        is LoadingEvent.Success -> {
                            val result = event.data
                            _uiState.update {
                                it.copy(
                                    shortenUrlState = SubmitLoadingState.Success(result),
                                    history = if (it.history.none { h -> h.alias == result.alias }) {
                                        listOf(result) + it.history
                                    } else {
                                        it.history
                                    },
                                    url = ""
                                )
                            }
                            _eventFlow.emit(UiEvent.ShowSnackbar("Shortened URL copied to clipboard!"))
                        }
                        is LoadingEvent.Error -> {
                            val errorMessage = when (event.exception) {
                                is UnknownHostException -> "Please check your internet connection."
                                else -> event.exception.message ?: "An unknown error occurred"
                            }
                            _uiState.update {
                                it.copy(
                                    shortenUrlState = SubmitLoadingState.Error(event.exception),
                                    networkError = errorMessage
                                )
                            }
                        }
                    }
                }
            }
        } else {
            _uiState.update { it.copy(urlError = "Invalid URL") }
        }
    }

    companion object {
        private const val SHORT_URL_BASE = "https://url-shortener-server.onrender.com/api/alias/"
    }
}