package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.news.ArticleDTO
import ru.asmelnikov.data.models.news.NewsDTO
import ru.asmelnikov.data.models.news.SourceDTO
import ru.asmelnikov.domain.models.Article
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.Source

fun NewsDTO.toNews(): News {
    return News(
        articles = articles?.map { it.toArticle() } ?: emptyList(),
        status = status ?: "",
        totalResults = totalResults ?: -1
    )
}

fun ArticleDTO.toArticle(): Article {
    return Article(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        source = this.source.toSource(),
        title = title ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: ""
    )
}

fun SourceDTO?.toSource(): Source {
    return Source(
        id = this?.id ?: "",
        name = this?.name ?: ""
    )
}