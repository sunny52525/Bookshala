package com.sunny.handbook.data


sealed class RequestResult<T>(val data: T? = null, val error: String? = null) {

    class Success<T>(
        data: T?
    ) : RequestResult<T>(data = data)

    class Error<T>(
        error: String?
    ) : RequestResult<T>(error = error)

    class Loading<T> : RequestResult<T>()
    class Idle<T> : RequestResult<T>()
}