package ru.asmelnikov.data.retrofit_errors_handler

import java.net.ConnectException
import java.net.HttpRetryException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.Response
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource

interface RetrofitErrorsHandler {
    suspend fun <T> executeSafely(
        block: suspend () -> Resource<T>,
    ): Resource<T>

    fun <T, R> responseFailureHandler(response: Response<T>): Resource.Error<R>

    class RetrofitErrorsHandlerImpl : RetrofitErrorsHandler {
        override suspend fun <T> executeSafely(block: suspend () -> Resource<T>): Resource<T> = try {
            block()
        } catch (e: Exception) {
            when (e) {
                is ConnectException, is SocketException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.MissingConnection())
                }

                is SocketTimeoutException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.TimeoutException())
                }

                is UnknownHostException, is HttpRetryException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.NetworkError(message = e.message))
                }

                else -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError(e.message))
                }
            }
        }

        override fun <T, R> responseFailureHandler(response: Response<T>): Resource.Error<R> = when (response.code()) {
            in 400..499 -> {
                Resource.Error(
                    httpErrors = ErrorsTypesHttp.Https400Errors(
                        errorCode = response.code(),
                    ),
                )
            }

            in 500..599 -> {
                Resource.Error(
                    httpErrors = ErrorsTypesHttp.Https500Errors(
                        errorCode = response.code(),
                    ),
                )
            }

            else -> {
                Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError(message = response.message()))
            }
        }
    }
}
