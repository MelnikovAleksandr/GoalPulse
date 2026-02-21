package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompetitionStandings(
    val id: String,
    val area: Area,
    val competition: Competition,
    val standings: List<Standing>
) : Parcelable

@Parcelize
data class Filters(
    val season: String
) : Parcelable

@Parcelize
data class Season(
    val currentMatchday: Int,
    val endDate: String,
    val startDateEndDate: String, // example - 2022/2023
    val id: Int,
    val startDate: String,
    val winner: Team
) : Parcelable

@Parcelize
data class Standing(
    val group: String,
    val stage: String,
    val table: List<Table>,
    val type: String
) : Parcelable

@Parcelize
data class Table(
    val draw: Int,
    val form: String,
    val goalDifference: Int,
    val goalsAgainst: Int,
    val goalsFor: Int,
    val lost: Int,
    val playedGames: Int,
    val points: Int,
    val position: Int,
    val team: Team,
    val won: Int
) : Parcelable

@Parcelize
data class Team(
    val crest: String,
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String
) : Parcelable