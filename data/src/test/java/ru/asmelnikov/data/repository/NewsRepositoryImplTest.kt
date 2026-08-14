package ru.asmelnikov.data.repository

import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.asmelnikov.data.api.NewsApi
import ru.asmelnikov.data.models.news.ArticleDTO
import ru.asmelnikov.data.models.news.NewsDTO
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.net.ConnectException

class NewsRepositoryImplTest {

    private lateinit var api: FakeNewsApi
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        api = FakeNewsApi()
        repository = NewsRepositoryImpl(
            newsApi = api,
            retrofitErrorsHandler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl()
        )
    }

    @Test
    fun success_returnsArticlesForQuery() = runBlocking {
        api.response = Response.success(
            NewsDTO(
                articles = listOf(
                    articleDto(title = "Arsenal win"),
                    articleDto(title = "Saka scores")
                ),
                status = "ok",
                totalResults = 2
            )
        )

        val result = repository.getNews("Arsenal")

        assertTrue(result is Resource.Success)
        assertEquals("Arsenal", api.requestedQuery)
        assertEquals(
            listOf("Arsenal win", "Saka scores"),
            result.data?.articles?.map { it.title }
        )
    }

    @Test
    fun successWithNullBody_returnsEmptyNews() = runBlocking {
        api.response = Response.success(null)

        val result = repository.getNews("Barcelona")

        assertTrue(result is Resource.Success)
        assertEquals(News(), result.data)
    }

    @Test
    fun notFound_returnsError() = runBlocking {
        api.response = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getNews("Chelsea")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
    }

    @Test
    fun noNetwork_returnsMissingConnection() = runBlocking {
        api.exception = ConnectException("failed to connect")

        val result = repository.getNews("Liverpool")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.MissingConnection(), result.httpErrors)
    }

    // region Factories

    private fun articleDto(title: String): ArticleDTO {
        return ArticleDTO(
            author = null,
            content = null,
            description = null,
            publishedAt = null,
            source = null,
            title = title,
            url = null,
            urlToImage = null
        )
    }

    // endregion

    // region Fakes

    private class FakeNewsApi : NewsApi {
        var response: Response<NewsDTO> = Response.success(null)
        var exception: Exception? = null
        var requestedQuery: String? = null

        override suspend fun getAllArticlesByQuery(
            query: String,
            language: String,
            sortBy: String,
            pageSize: Int
        ): Response<NewsDTO> {
            exception?.let { throw it }
            requestedQuery = query
            return response
        }
    }

    // endregion
}
