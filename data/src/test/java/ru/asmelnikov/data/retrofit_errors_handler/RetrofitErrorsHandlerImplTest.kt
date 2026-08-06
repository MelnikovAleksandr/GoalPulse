package ru.asmelnikov.data.retrofit_errors_handler

import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.net.ConnectException
import java.net.HttpRetryException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitErrorsHandlerImplTest {

    private lateinit var handler: RetrofitErrorsHandler

    @Before
    fun setUp() {
        handler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl()
    }

    // region executeSafely

    @Test
    fun executeSafely_returnsBlockResult_whenNoException() = runBlocking {
        val expected = Resource.Success("ok")

        val result = handler.executeSafely { expected }

        assertEquals(expected, result)
    }

    @Test
    fun executeSafely_mapsConnectException_toMissingConnection() = runBlocking {
        val result = handler.executeSafely<String> { throw ConnectException("failed to connect") }

        assertEquals(ErrorsTypesHttp.MissingConnection(), result.requireHttpError())
    }

    @Test
    fun executeSafely_mapsSocketException_toMissingConnection() = runBlocking {
        val result = handler.executeSafely<String> { throw SocketException("socket closed") }

        assertEquals(ErrorsTypesHttp.MissingConnection(), result.requireHttpError())
    }

    @Test
    fun executeSafely_mapsSocketTimeout_toTimeoutException() = runBlocking {
        val result = handler.executeSafely<String> { throw SocketTimeoutException("timed out") }

        assertEquals(ErrorsTypesHttp.TimeoutException(), result.requireHttpError())
    }

    @Test
    fun executeSafely_mapsUnknownHost_toNetworkError() = runBlocking {
        val result = handler.executeSafely<String> {
            throw UnknownHostException("Unable to resolve host")
        }

        assertEquals(
            ErrorsTypesHttp.NetworkError(message = "Unable to resolve host"),
            result.requireHttpError()
        )
    }

    @Test
    fun executeSafely_mapsHttpRetry_toNetworkError() = runBlocking {
        val result = handler.executeSafely<String> {
            throw HttpRetryException("cannot retry", 503)
        }

        assertEquals(
            ErrorsTypesHttp.NetworkError(message = "cannot retry"),
            result.requireHttpError()
        )
    }

    @Test
    fun executeSafely_mapsUnexpectedException_toUnknownError() = runBlocking {
        val result = handler.executeSafely<String> { throw IllegalStateException("boom") }

        assertEquals(
            ErrorsTypesHttp.UnknownError(message = "boom"),
            result.requireHttpError()
        )
    }

    // endregion

    // region responseFailureHandler

    @Test
    fun responseFailureHandler_mapsClientError_toHttps400Errors() {
        val result = handler.responseFailureHandler<Any, Any>(errorResponse(404))

        assertEquals(
            ErrorsTypesHttp.Https400Errors(errorCode = 404),
            result.requireHttpError()
        )
    }

    @Test
    fun responseFailureHandler_mapsAnotherClientError_toHttps400Errors() {
        val result = handler.responseFailureHandler<Any, Any>(errorResponse(418))

        assertEquals(
            ErrorsTypesHttp.Https400Errors(errorCode = 418),
            result.requireHttpError()
        )
    }

    @Test
    fun responseFailureHandler_mapsServerError_toHttps500Errors() {
        val result = handler.responseFailureHandler<Any, Any>(errorResponse(500))

        assertEquals(
            ErrorsTypesHttp.Https500Errors(errorCode = 500),
            result.requireHttpError()
        )
    }

    @Test
    fun responseFailureHandler_mapsAnotherServerError_toHttps500Errors() {
        val result = handler.responseFailureHandler<Any, Any>(errorResponse(503))

        assertEquals(
            ErrorsTypesHttp.Https500Errors(errorCode = 503),
            result.requireHttpError()
        )
    }

    @Test
    fun responseFailureHandler_mapsOtherCode_toUnknownError() {
        val body = ByteArray(0).toResponseBody(null)
        val response = Response.error<Any>(
            body,
            okhttp3.Response.Builder()
                .request(okhttp3.Request.Builder().url("http://localhost/").build())
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(302)
                .message("Found")
                .body(body)
                .build()
        )

        val result = handler.responseFailureHandler<Any, Any>(response)

        assertEquals(
            ErrorsTypesHttp.UnknownError(message = response.message()),
            result.requireHttpError()
        )
    }

    // endregion

    // region Helpers

    private fun errorResponse(code: Int): Response<Any> {
        return Response.error(code, ByteArray(0).toResponseBody(null))
    }

    private fun <T> Resource<T>.requireHttpError(): ErrorsTypesHttp {
        assertTrue("Expected Resource.Error, was $this", this is Resource.Error)
        val error = httpErrors
        assertTrue("Expected httpErrors, was null", error != null)
        return error!!
    }

    // endregion
}
