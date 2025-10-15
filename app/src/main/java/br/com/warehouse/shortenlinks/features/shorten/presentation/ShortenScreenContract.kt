package br.com.warehouse.shortenlinks.features.shorten.presentation

import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

/**
 * Represents the state of the [ShortenScreen].
 *
 * @property url The URL input by the user.
 * @property urlError The error message for the URL input.
 * @property shortenUrlState The state of the shorten URL operation.
 * @property history The history of shortened URLs.
 * @property networkError The network error message.
 */
data class ShortenScreenState(
    val url: String = "",
    val urlError: String? = null,
    val shortenUrlState: SubmitLoadingState<Shorten> = SubmitLoadingState.Idle,
    val history: List<Shorten> = emptyList(),
    val networkError: String? = null
)

/**
 * Represents the events that can be triggered by the user on the [ShortenScreen].
 */
sealed class ShortenScreenEvent {
    /**
     * Event triggered when the URL input changes.
     *
     * @property url The new URL.
     */
    data class OnUrlChange(val url: String) : ShortenScreenEvent()

    /**
     * Event triggered when the shorten button is clicked.
     */
    object OnShortenClick : ShortenScreenEvent()

    /**
     * Event triggered when the clear button is clicked.
     */
    object OnClearClick : ShortenScreenEvent()

    /**
     * Event triggered when the error dialog is dismissed.
     */
    object OnDismissErrorDialog : ShortenScreenEvent()
}

/**
 * Represents the UI events that can be triggered by the [ShortenViewModel].
 */
sealed class UiEvent {
    /**
     * Event to show a snackbar.
     *
     * @property message The message to be shown in the snackbar.
     */
    data class ShowSnackbar(val message: String) : UiEvent()
}
