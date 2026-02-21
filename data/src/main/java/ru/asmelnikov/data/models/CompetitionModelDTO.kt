package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionModelDTO(
    @Json(name = "competitions") val competitions: List<CompetitionDTO>?
)

@JsonClass(generateAdapter = true)
data class CompetitionDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "code") val code: String?,
    @Json(name = "currentSeason") val currentSeason: CurrentSeasonDTO?,
    @Json(name = "emblem") val emblem: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "type") val type: String?
)

@JsonClass(generateAdapter = true)
data class AreaDTO(
    @Json(name = "code") val code: String?,
    @Json(name = "flag") val flag: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class CurrentSeasonDTO(
    @Json(name = "currentMatchday") val currentMatchDay: Int?,
    @Json(name = "endDate") val endDate: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "startDate") val startDate: String?,
    @Json(name = "winner") val winner: TeamInfoDTO?
)
