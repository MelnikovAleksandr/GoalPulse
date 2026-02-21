package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionStandingsModelDTO(
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "season") val season: CurrentSeasonDTO?,
    @Json(name = "standings") val standings: List<StandingDTO>?
)

@JsonClass(generateAdapter = true)
data class StandingDTO(
    @Json(name = "group") val group: String?,
    @Json(name = "stage") val stage: String?,
    @Json(name = "table") val table: List<TableDTO>?,
    @Json(name = "type") val type: String?
)

@JsonClass(generateAdapter = true)
data class TableDTO(
    @Json(name = "draw") val draw: Int?,
    @Json(name = "form") val form: String?,
    @Json(name = "goalDifference") val goalDifference: Int?,
    @Json(name = "goalsAgainst") val goalsAgainst: Int?,
    @Json(name = "goalsFor") val goalsFor: Int?,
    @Json(name = "lost") val lost: Int?,
    @Json(name = "playedGames") val playedGames: Int?,
    @Json(name = "points") val points: Int?,
    @Json(name = "position") val position: Int?,
    @Json(name = "team") val team: TeamInfoDTO?,
    @Json(name = "won") val won: Int?
)
