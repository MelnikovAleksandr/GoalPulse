package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompetitionStandings(
    val id: String = UUID.randomUUID().toString(),
    val area: Area = Area(),
    val competition: Competition = Competition(),
    val standings: List<Standing>,
) : Parcelable

@Parcelize
data class Standing(
    val group: Group = Group.NON,
    val stage: Stage = Stage.NON,
    val table: List<Table> = emptyList(),
    val type: TournamentType = TournamentType.NON,
) : Parcelable

@Parcelize
data class Table(
    val draw: Int = 0,
    val form: String = "",
    val goalDifference: Int = 0,
    val goalsAgainst: Int = 0,
    val goalsFor: Int = 0,
    val lost: Int = 0,
    val playedGames: Int = 0,
    val points: Int = 0,
    val position: Int = 0,
    val team: Team = Team(),
    val won: Int = 0,
) : Parcelable

@Parcelize
data class Team(
    val id: Int = UUID.randomUUID().hashCode(),
    val crest: String = "",
    val name: String = "",
    val shortName: String = "",
    val tla: String = "",
) : Parcelable
