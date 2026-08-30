package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchesDTO(
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "matches") val matches: List<MatchDTO>?,
)

@JsonClass(generateAdapter = true)
data class MatchDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "awayTeam") val awayTeam: TeamInfoDTO?,
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "group") val group: String?,
    @Json(name = "homeTeam") val homeTeam: TeamInfoDTO?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "matchday") val matchDay: Int?,
    @Json(name = "referees") val referees: List<RefereeDTO>?,
    @Json(name = "score") val score: ScoreDTO?,
    @Json(name = "season") val season: CurrentSeasonDTO?,
    @Json(name = "stage") val stage: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "utcDate") val utcDate: String?,
)

@JsonClass(generateAdapter = true)
data class RefereeDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "type") val type: String?,
)

@JsonClass(generateAdapter = true)
data class ScoreDTO(
    @Json(name = "duration") val duration: String?,
    @Json(name = "fullTime") val fullTime: TimeDTO?,
    @Json(name = "halfTime") val halfTime: TimeDTO?,
    @Json(name = "winner") val winner: String?,
)

@JsonClass(generateAdapter = true)
data class TimeDTO(@Json(name = "away") val away: Int?, @Json(name = "home") val home: Int?)
