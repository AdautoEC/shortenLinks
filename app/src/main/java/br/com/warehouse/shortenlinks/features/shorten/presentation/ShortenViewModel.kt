package br.com.warehouse.shortenlinks.features.shorten.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.warehouse.shortenlinks.core.presentation.model.LoadingEvent
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortenViewModel @Inject constructor(
    private val shortenUrlUseCase: ShortenUrlUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShortenScreenState())
    val uiState: StateFlow<ShortenScreenState> = _uiState.asStateFlow()

    fun onEvent(event: ShortenScreenEvent) {
        when (event) {
            is ShortenScreenEvent.OnUrlChange -> {
                _uiState.update { it.copy(url = event.url) }
            }
            is ShortenScreenEvent.OnShortenClick -> {
                shortenUrl()
            }
            is ShortenScreenEvent.OnClearClick -> {
                _uiState.update { it.copy(url = "") }
            }
        }
    }

    private fun shortenUrl() {
        val url = _uiState.value.url
        if (Patterns.WEB_URL.matcher(url).matches()) {
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
                                    }
                                )
                            }
                        }
                        is LoadingEvent.Error -> {
                            _uiState.update { it.copy(shortenUrlState = SubmitLoadingState.Error(event.exception)) }
                        }
                    }
                }
            }
        } else {
            _uiState.update { it.copy(urlError = "Invalid URL") }
        }
    }
}