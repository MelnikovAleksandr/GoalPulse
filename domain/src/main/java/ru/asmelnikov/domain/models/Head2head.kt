package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Head2head(
    val id: Int = -1,
    val aggregates: Aggregates = Aggregates()
) : Parcelable

@Parcelize
data class Aggregates(
    val awayTeam: TeamH2H = TeamH2H(),
    val homeTeam: TeamH2H = TeamH2H(),
    val homeWinsPercentage: Float = -1f,
    val awayWinsPercentage: Float = -1f,
    val drawsPercentage: Float = -1f,
    val numberOfMatches: Int = -1,
    val totalGoals: Int = -1
) : Parcelable

@Parcelize
data class TeamH2H(
    val draws: Int = -1,
    val id: Int = -1,
    val losses: Int = -1,
    val name: String = "",
    val wins: Int = -1
) : Parcelable