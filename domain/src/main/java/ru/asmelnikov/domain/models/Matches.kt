package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Matches(
    val id: String = "",
    val matchesByTourCompleted: List<MatchesByTour> = emptyList(),
    val matchesByTourAhead: List<MatchesByTour> = emptyList(),
) : Parcelable

@Parcelize
data class MatchesByTour(
    val matchDay: Int = 0,
    val seasonType: TournamentType = TournamentType.NON,
    val stage: Stage = Stage.NON,
    val matches: List<Match> = emptyList(),
) : Parcelable

@Parcelize
data class Match(
    val id: Int = UUID.randomUUID().hashCode(),
    val area: Area = Area(),
    val competition: Competition = Competition(),
    val awayTeam: MatchTeam = MatchTeam(),
    val group: Group = Group.NON,
    val homeTeam: MatchTeam = MatchTeam(),
    val matchDay: Int = 0,
    val referees: List<Referee> = emptyList(),
    val score: Score = Score(),
    val stage: Stage = Stage.NON,
    val status: MatchStatus = MatchStatus.NON,
    val utcDate: String = "",
    val bigDate: String = "",
    val startEpochMillis: Long = 0L,
) : Parcelable

@Parcelize
data class MatchTeam(
    val id: Int = UUID.randomUUID().hashCode(),
    val crest: String = "",
    val name: String = "",
    val shortName: String = "",
    val tla: String = "",
) : Parcelable

@Parcelize
data class Referee(
    val id: Int = UUID.randomUUID().hashCode(),
    val name: String = "",
    val nationality: String = "",
    val type: String = "",
) : Parcelable

@Parcelize
data class Score(
    val duration: String = "",
    val fullTime: Time = Time(),
    val halfTime: Time = Time(),
    val winner: Winner = Winner.NON,
) : Parcelable

@Parcelize
data class Time(val away: Int = 0, val home: Int = 0) : Parcelable
