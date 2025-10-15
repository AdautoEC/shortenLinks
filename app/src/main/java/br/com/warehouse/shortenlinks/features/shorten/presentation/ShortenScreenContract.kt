package br.com.warehouse.shortenlinks.features.shorten.presentation

import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

data class ShortenScreenState(
    val url: String = "",
    val urlError: String? = null,
    val shortenUrlState: SubmitLoadingState<Shorten> = SubmitLoadingState.Idle,
    val history: List<Shorten> = emptyList(),
    val networkError: String? = null
)

sealed class ShortenScreenEvent {
    data class OnUrlChange(val url: String) : ShortenScreenEvent()
    object OnShortenClick : ShortenScreenEvent()
    object OnClearClick : ShortenScreenEvent()
    object OnDismissErrorDialog : ShortenScreenEvent()
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}
