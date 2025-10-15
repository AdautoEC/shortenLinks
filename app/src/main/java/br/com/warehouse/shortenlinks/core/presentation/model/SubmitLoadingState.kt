package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A sealed interface representing the different states of a submission operation.
 *
 * @param T The type of the data being submitted.
 */
sealed interface SubmitLoadingState<out T> {

    /**
     * Represents the idle state, before any submission has been made.
     */
    data object Idle : SubmitLoadingState<Nothing>
    /**
     * Represents the loading state, while the submission is in progress.
     */
    data object Loading : SubmitLoadingState<Nothing>

    /**
     * Represents a successful submission.
     *
     * @param data The data that was submitted.
     */
    data class Success<T>(val data: T) : SubmitLoadingState<T>

    /**
     * Represents a failed submission.
     *
     * @param exception The exception that occurred.
     */
    @JvmInline
    value class Error(val exception: Throwable) : SubmitLoadingState<Nothing>
}


/**
 * Aggregates a [Flow] of [LoadingEvent]s into a [Flow] of [SubmitLoadingState]s.
 *
 * This is useful for converting the event-based loading state into a state-based one.
 */
internal fun <T> Flow<LoadingEvent<T>?>.aggregateEventsToSubmitLoadingState(): Flow<SubmitLoadingState<T>> =
    map { event ->
        when (event) {
            is LoadingEvent.Loading -> SubmitLoadingState.Loading
            is LoadingEvent.Error -> SubmitLoadingState.Error(event.exception)
            is LoadingEvent.Success -> SubmitLoadingState.Success(event.data)
            else -> SubmitLoadingState.Idle
        }
    }

/**
 * Maps a [SubmitLoadingState] of type [T] to a [SubmitLoadingState] of type [R].
 *
 * @param transform The function to transform the data from [T] to [R].
 * @return A new [SubmitLoadingState] with the transformed data.
 */
inline fun <T, R> SubmitLoadingState<T>.map(transform: (T) -> R): SubmitLoadingState<R> = when (this) {
    is SubmitLoadingState.Error -> this
    is SubmitLoadingState.Loading -> this
    is SubmitLoadingState.Idle -> this
    is SubmitLoadingState.Success -> SubmitLoadingState.Success(transform(this.data))
}

/**
 * Maps the success data of a [Flow] of [SubmitLoadingState]s.
 *
 * @param transform The function to transform the success data.
 * @return A new [Flow] of [SubmitLoadingState]s with the transformed success data.
 */
@Suppress("unused")
inline fun <T, R> Flow<SubmitLoadingState<T>>.mapSuccess(
    crossinline transform: (T) -> R,
): Flow<SubmitLoadingState<R>> {
    return map {
        it.map { successData ->
            transform(successData)
        }
    }
}

/**
 * Returns `true` if the [SubmitLoadingState] is [SubmitLoadingState.Loading].
 */
fun <T> SubmitLoadingState<T>.isLoading() = this is SubmitLoadingState.Loading

/**
 * Returns `true` if the [SubmitLoadingState] is [SubmitLoadingState.Success].
 */
fun <T> SubmitLoadingState<T>.isSuccess() = this is SubmitLoadingState.Success

/**
 * Returns `true` if the [SubmitLoadingState] is [SubmitLoadingState.Error].
 */
fun <T> SubmitLoadingState<T>.isError() = this is SubmitLoadingState.Error

/**
 * Returns the success data if the [SubmitLoadingState] is [SubmitLoadingState.Success], otherwise `null`.
 */
fun <T> SubmitLoadingState<T>.getSuccessDataOrNull(): T?{
    if(this.isSuccess()){
        return (this as SubmitLoadingState.Success).data
    }
    return null
}

/**
 * Returns the success data if the [SubmitLoadingState] is [SubmitLoadingState.Success].
 *
 * @throws ClassCastException if the state is not a [SubmitLoadingState.Success].
 */
@Suppress("unused")
fun <T> SubmitLoadingState<T>.getSuccessData(): T =
    (this as SubmitLoadingState.Success).data

/**
 * Returns the exception if the [SubmitLoadingState] is [SubmitLoadingState.Error], otherwise `null`.
 */
fun <T> SubmitLoadingState<T>.getErrorThrowableOrNull(): Throwable?{
    if(this.isError()){
        return (this as SubmitLoadingState.Error).exception
    }
    return null
}
