package ru.asmelnikov.domain.repository

import ru.asmelnikov.domain.models.News
import ru.asmelnikov.utils.Resource

interface NewsRepository {

    suspend fun getNews(q: String): Resource<News>
}
