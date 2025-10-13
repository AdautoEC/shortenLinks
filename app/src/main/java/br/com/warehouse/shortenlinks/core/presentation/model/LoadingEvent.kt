package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface LoadingEvent<out T> {
    data object Loading : LoadingEvent<Nothing>

    sealed interface Result<out T> : LoadingEvent<T>

    data class Success<out T>(val data: T) : Result<T>

    @JvmInline
    value class Error(val exception: Throwable) : Result<Nothing>
}

inline fun <T, R> LoadingEvent<T>.map(transform: (T) -> R): LoadingEvent<R> = when (this) {
    is LoadingEvent.Error -> this
    is LoadingEvent.Loading -> this
    is LoadingEvent.Success -> LoadingEvent.Success(transform(this.data))
}

inline fun <T, R> Flow<LoadingEvent<T>>.mapSuccess(
    crossinline transform: (T) -> R,
): Flow<LoadingEvent<R>> {
    return map {
        it.map { successData ->
            transform(successData)
        }
    }
}

@Suppress("unused")
fun <T> LoadingEvent<T>.isLoading() = this is LoadingEvent.Loading
fun <T> LoadingEvent<T>.isSuccess() = this is LoadingEvent.Success
fun <T> LoadingEvent<T>.isError() = this is LoadingEvent.Error

@Suppress("unused")
fun <T> LoadingEvent<T>.getSuccessDataOrNull(): T?{
    if(this.isSuccess()){
        return (this as LoadingEvent.Success).data
    }
    return null
}

@Suppress("unused")
fun <T> LoadingEvent<T>.getSuccessData(): T =
    (this as LoadingEvent.Success).data

@Suppress("unused")
fun <T> LoadingEvent<T>.getErrorThrowableOrNull(): Throwable?{
    if(this.isError()){
        return (this as LoadingEvent.Error).exception
    }
    return null
}
