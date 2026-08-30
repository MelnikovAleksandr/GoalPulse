package ru.asmelnikov.data.repository

import ru.asmelnikov.data.api.NewsApi
import ru.asmelnikov.data.mappers.toNews
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.repository.NewsRepository
import ru.asmelnikov.utils.Resource

class NewsRepositoryImpl(private val newsApi: NewsApi, private val retrofitErrorsHandler: RetrofitErrorsHandler) :
    NewsRepository {

    override suspend fun getNews(q: String): Resource<News> = retrofitErrorsHandler.executeSafely {
        val response = newsApi.getAllArticlesByQuery(query = q)
        if (response.isSuccessful && response.code() == 200) {
            Resource.Success(response.body()?.toNews() ?: News())
        } else {
            retrofitErrorsHandler.responseFailureHandler(response)
        }
    }
}
