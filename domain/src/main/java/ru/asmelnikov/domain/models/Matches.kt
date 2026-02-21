package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Matches(
    val id: String = "",
    val matchesByTourCompleted: List<MatchesByTour> = emptyList(),
    val matchesByTourAhead: List<MatchesByTour> = emptyList()
) : Parcelable

@Parcelize
data class MatchesByTour(
    val matchday: Int,
    val seasonType: String,
    val stage: String?,
    val matches: List<Match>
) : Parcelable

@Parcelize
data class Match(
    val area: Area,
    val competition: Competition,
    val awayTeam: MatchTeam,
    val group: String,
    val homeTeam: MatchTeam,
    val id: Int,
    val matchDay: Int,
    val referees: List<Referee>,
    val score: Score,
    val stage: String,
    val status: String,
    val utcDate: String,
    val bigDate: String
) : Parcelable

@Parcelize
data class MatchTeam(
    val crest: String,
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String
) : Parcelable

@Parcelize
data class Referee(
    val id: Int,
    val name: String,
    val nationality: String,
    val type: String
) : Parcelable

@Parcelize
data class Score(
    val duration: String,
    val fullTime: Time,
    val halfTime: Time,
    val winner: String
) : Parcelable

@Parcelize
data class Time(
    val away: Int,
    val home: Int
) : Parcelable