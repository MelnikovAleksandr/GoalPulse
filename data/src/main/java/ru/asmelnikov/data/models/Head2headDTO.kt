package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Head2headDTO(
    @Json(name = "aggregates") val aggregates: AggregatesDTO?
)

@JsonClass(generateAdapter = true)
data class AggregatesDTO(
    @Json(name = "awayTeam") val awayTeam: TeamH2HDTO?,
    @Json(name = "homeTeam") val homeTeam: TeamH2HDTO?,
    @Json(name = "numberOfMatches") val numberOfMatches: Int?,
    @Json(name = "totalGoals") val totalGoals: Int?
)

@JsonClass(generateAdapter = true)
data class TeamH2HDTO(
    @Json(name = "draws") val draws: Int?,
    @Json(name = "id") val id: Int?,
    @Json(name = "losses") val losses: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "wins") val wins: Int?
)
