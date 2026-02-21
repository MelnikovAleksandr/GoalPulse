package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMatches(
    val id: String = "",
    val season: String = "",
    val matchesCompleted: List<Match> = emptyList(),
    val matchesAhead: List<Match> = emptyList()
) : Parcelable