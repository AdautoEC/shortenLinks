package br.com.warehouse.shortenlinks.core.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * An interface representing a submittable operation.
 *
 * @param T The type of the data being submitted.
 */
interface Submittable<T> {

    /**
     * A [Flow] of [SubmitLoadingState]s representing the state of the submission.
     */
    val flow: Flow<SubmitLoadingState<T>>

    /**
     * Submits the operation.
     */
    fun submit()

    /**
     * Resets the submission state to its initial value.
     */
    fun reset()
}

internal class SubmittableImpl<P, T>(
    params: Flow<P>,
    private val scope: CoroutineScope,
    started: SharingStarted,
    private val autoSubmit: Boolean,
    initialValue: SubmitLoadingState<T>,
    createSubmitEvents: (params: P) -> Flow<LoadingEvent<T>?>,
) : Submittable<T> {

    private val event = MutableSharedFlow<SubmittableEvent>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val flow = params
        .flatMapLatest { p ->
            event.flatMapLatest {
                createSubmittable(
                    event = it,
                    params = p,
                    createSubmitEvents = createSubmitEvents,
                )
            }.onStart {
                if (autoSubmit) {
                    emitAll(
                        createSubmittable(
                            event = SubmittableEvent.Submit,
                            params = p,
                            createSubmitEvents = createSubmitEvents,
                        ),
                    )
                } else {
                    emit(initialValue)
                }
            }
        }
        .stateIn(
            scope = scope,
            started = started,
            initialValue = initialValue
        )

    private fun createSubmittable(
        event: SubmittableEvent,
        params: P,
        createSubmitEvents: (params: P) -> Flow<LoadingEvent<T>?>,
    ): Flow<SubmitLoadingState<T>> {
        return when (event) {
            SubmittableEvent.Submit -> createSubmitEvents(params)
            SubmittableEvent.Reset -> flowOf(null)
        }.aggregateEventsToSubmitLoadingState()
    }

    override fun submit() {
        scope.launch {
            event.emit(SubmittableEvent.Submit)
        }
    }

    override fun reset() {
        scope.launch {
            event.emit(SubmittableEvent.Reset)
        }
    }

    enum class SubmittableEvent {
        Submit,
        Reset
    }
}

/**
 *  Creates a [Submittable] out of the given input parameters and the flow of loading events that
 *  happen during submission.
 * It provides a Flow of [SubmitLoadingState]s (stated in the ViewModel) and a `submit()` method.
 * It will execute whenever `submit()` is called and will reset its state when the input changes.
 */
@Suppress("unused")
fun <P, T : Any> ViewModel.submittable(
    params: Flow<P>,
    scope: CoroutineScope = viewModelScope,
    started: SharingStarted = SharingStarted.Lazily,
    autoSubmit: Boolean = false,
    initialValue: SubmitLoadingState<T> = SubmitLoadingState.Idle,
    createSubmitEvents: (params: P) -> Flow<LoadingEvent<T>?>,
): Submittable<T> = SubmittableImpl(
    params = params,
    scope = scope,
    started = started,
    initialValue = initialValue,
    createSubmitEvents = createSubmitEvents,
    autoSubmit = autoSubmit,
)

/**
 *  Creates a [Submittable] out of a flow of loading events that happen during submission. It does
 *  not take any input parameters.
 * It provides a Flow of [SubmitLoadingState]s (stated in the ViewModel) and a `submit()` method.
 * It will execute whenever `submit()` is called.
 */
@Suppress("unused")
fun <T> ViewModel.submittable(
    scope: CoroutineScope = viewModelScope,
    started: SharingStarted = SharingStarted.Lazily,
    autoSubmit: Boolean = false,
    initialValue: SubmitLoadingState<T> = SubmitLoadingState.Idle,
    createSubmitEvents: () -> Flow<LoadingEvent<T>?>,
): Submittable<T> = SubmittableImpl(
    params = flowOf(Unit),
    scope = scope,
    started = started,
    initialValue = initialValue,
    createSubmitEvents = { createSubmitEvents() },
    autoSubmit = autoSubmit,
)
