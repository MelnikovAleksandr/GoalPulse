package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CompetitionScorers(
    val id: String = UUID.randomUUID().toString(),
    val season: CurrentSeason = CurrentSeason(),
    val scorers: List<Scorer> = emptyList()
) : Parcelable

@Parcelize
data class Scorer(
    val assists: Int = 0,
    val goals: Int = 0,
    val penalties: Int = 0,
    val playedMatches: Int = 0,
    val player: Player = Player(),
    val team: Team = Team()
) : Parcelable

@Parcelize
data class Player(
    val id: Int = UUID.randomUUID().hashCode(),
    val dateOfBirth: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "",
    val nationality: String = "",
    val position: PlayerPosition = PlayerPosition.NON,
    val shirtNumber: Int = -1
) : Parcelable
