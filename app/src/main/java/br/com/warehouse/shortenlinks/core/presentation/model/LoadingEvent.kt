package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A sealed interface representing the different states of a loading operation.
 *
 * @param T The type of the data being loaded.
 */
sealed interface LoadingEvent<out T> {
    /**
     * Represents the loading state.
     */
    data object Loading : LoadingEvent<Nothing>

    /**
     * A sealed interface representing the result of a loading operation.
     */
    sealed interface Result<out T> : LoadingEvent<T>

    /**
     * Represents a successful loading operation.
     *
     * @param data The loaded data.
     */
    data class Success<out T>(val data: T) : Result<T>

    /**
     * Represents a failed loading operation.
     *
     * @param exception The exception that occurred.
     */
    @JvmInline
    value class Error(val exception: Throwable) : Result<Nothing>
}

/**
 * Maps a [LoadingEvent] of type [T] to a [LoadingEvent] of type [R].
 *
 * @param transform The function to transform the data from [T] to [R].
 * @return A new [LoadingEvent] with the transformed data.
 */
inline fun <T, R> LoadingEvent<T>.map(transform: (T) -> R): LoadingEvent<R> = when (this) {
    is LoadingEvent.Error -> this
    is LoadingEvent.Loading -> this
    is LoadingEvent.Success -> LoadingEvent.Success(transform(this.data))
}

/**
 * Maps the success data of a [Flow] of [LoadingEvent]s.
 *
 * @param transform The function to transform the success data.
 * @return A new [Flow] of [LoadingEvent]s with the transformed success data.
 */
inline fun <T, R> Flow<LoadingEvent<T>>.mapSuccess(
    crossinline transform: (T) -> R,
): Flow<LoadingEvent<R>> {
    return map {
        it.map { successData ->
            transform(successData)
        }
    }
}

/**
 * Returns `true` if the [LoadingEvent] is [LoadingEvent.Loading].
 */
@Suppress("unused")
fun <T> LoadingEvent<T>.isLoading() = this is LoadingEvent.Loading

/**
 * Returns `true` if the [LoadingEvent] is [LoadingEvent.Success].
 */
fun <T> LoadingEvent<T>.isSuccess() = this is LoadingEvent.Success

/**
 * Returns `true` if the [LoadingEvent] is [LoadingEvent.Error].
 */
fun <T> LoadingEvent<T>.isError() = this is LoadingEvent.Error

/**
 * Returns the success data if the [LoadingEvent] is [LoadingEvent.Success], otherwise `null`.
 */
@Suppress("unused")
fun <T> LoadingEvent<T>.getSuccessDataOrNull(): T?{
    if(this.isSuccess()){
        return (this as LoadingEvent.Success).data
    }
    return null
}

/**
 * Returns the success data if the [LoadingEvent] is [LoadingEvent.Success].
 *
 * @throws ClassCastException if the event is not a [LoadingEvent.Success].
 */
@Suppress("unused")
fun <T> LoadingEvent<T>.getSuccessData(): T =
    (this as LoadingEvent.Success).data

/**
 * Returns the exception if the [LoadingEvent] is [LoadingEvent.Error], otherwise `null`.
 */
@Suppress("unused")
fun <T> LoadingEvent<T>.getErrorThrowableOrNull(): Throwable?{
    if(this.isError()){
        return (this as LoadingEvent.Error).exception
    }
    return null
}
