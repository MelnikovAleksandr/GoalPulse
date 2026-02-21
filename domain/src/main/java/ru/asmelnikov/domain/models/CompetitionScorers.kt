package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompetitionScorers(
    val id: String,
    val season: Season,
    val scorers: List<Scorer>
) : Parcelable

@Parcelize
data class Scorer(
    val assists: Int,
    val goals: Int,
    val penalties: Int,
    val playedMatches: Int,
    val player: Player,
    val team: Team
) : Parcelable

@Parcelize
data class Player(
    val dateOfBirth: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val name: String,
    val nationality: String,
    val position: String,
    val shirtNumber: Int
) : Parcelable
