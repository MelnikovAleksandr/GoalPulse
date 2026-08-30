package ru.asmelnikov.utils

sealed class ErrorsTypesHttp(val code: Int? = null, val errorMessage: String? = null) {
    data class Https400Errors(val errorCode: Int? = null, val message: String? = null) :
        ErrorsTypesHttp(code = errorCode, errorMessage = message)
    data class Https500Errors(val errorCode: Int? = null, val message: String? = null) :
        ErrorsTypesHttp(code = errorCode, errorMessage = message)
    data class TimeoutException(val message: String? = null) : ErrorsTypesHttp(errorMessage = message)
    data class MissingConnection(val message: String? = null) : ErrorsTypesHttp(errorMessage = message)
    data class NetworkError(val message: String? = null) : ErrorsTypesHttp(errorMessage = message)
    data class UnknownError(val message: String? = null) : ErrorsTypesHttp(errorMessage = message)
}
