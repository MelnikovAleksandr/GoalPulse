package ru.asmelnikov.utils

sealed class Resource<T>(val data: T? = null, val httpErrors: ErrorsTypesHttp? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(data: T? = null, httpErrors: ErrorsTypesHttp? = null) : Resource<T>(data, httpErrors)
}
