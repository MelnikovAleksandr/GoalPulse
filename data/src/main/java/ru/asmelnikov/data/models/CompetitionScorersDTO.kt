package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionScorersModelDTO(
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "season") val season: CurrentSeasonDTO?,
    @Json(name = "scorers") val scorers: List<ScorerDTO>?
)

@JsonClass(generateAdapter = true)
data class ScorerDTO(
    @Json(name = "assists") val assists: Int?,
    @Json(name = "goals") val goals: Int?,
    @Json(name = "penalties") val penalties: Int?,
    @Json(name = "playedMatches") val playedMatches: Int?,
    @Json(name = "player") val player: PersonDTO?,
    @Json(name = "team") val team: TeamInfoDTO?
)