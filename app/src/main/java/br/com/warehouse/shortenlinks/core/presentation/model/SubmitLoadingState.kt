package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface SubmitLoadingState<out T> {

    data object Idle : SubmitLoadingState<Nothing>
    data object Loading : SubmitLoadingState<Nothing>

    data class Success<T>(val data: T) : SubmitLoadingState<T>

    @JvmInline
    value class Error(val exception: Throwable) : SubmitLoadingState<Nothing>
}


/**
 * Combine the previous exclusive api state with the latest one with a runningFold.
 * Keeps the data when the Flow switches from data to loading again.
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

inline fun <T, R> SubmitLoadingState<T>.map(transform: (T) -> R): SubmitLoadingState<R> = when (this) {
    is SubmitLoadingState.Error -> this
    is SubmitLoadingState.Loading -> this
    is SubmitLoadingState.Idle -> this
    is SubmitLoadingState.Success -> SubmitLoadingState.Success(transform(this.data))
}

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

fun <T> SubmitLoadingState<T>.isLoading() = this is SubmitLoadingState.Loading
fun <T> SubmitLoadingState<T>.isSuccess() = this is SubmitLoadingState.Success
fun <T> SubmitLoadingState<T>.isError() = this is SubmitLoadingState.Error

fun <T> SubmitLoadingState<T>.getSuccessDataOrNull(): T?{
    if(this.isSuccess()){
        return (this as SubmitLoadingState.Success).data
    }
    return null
}

@Suppress("unused")
fun <T> SubmitLoadingState<T>.getSuccessData(): T =
    (this as SubmitLoadingState.Success).data

fun <T> SubmitLoadingState<T>.getErrorThrowableOrNull(): Throwable?{
    if(this.isError()){
        return (this as SubmitLoadingState.Error).exception
    }
    return null
}
