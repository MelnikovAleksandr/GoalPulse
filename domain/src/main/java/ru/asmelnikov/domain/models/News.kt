package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(val articles: List<Article> = emptyList(), val status: String = "", val totalResults: Int = -1) :
    Parcelable

@Parcelize
data class Source(val id: String = "", val name: String = "") : Parcelable

@Parcelize
data class Article(
    val author: String = "",
    val content: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val source: Source = Source(),
    val title: String = "",
    val url: String = "",
    val urlToImage: String = "",
) : Parcelable
