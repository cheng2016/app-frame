package com.cds.iot.core.result

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val message: String, val code: String? = null) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()
}

inline fun <T> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) block(data)
    return this
}

inline fun <T> AppResult<T>.onError(block: (String) -> Unit): AppResult<T> {
    if (this is AppResult.Error) block(message)
    return this
}
