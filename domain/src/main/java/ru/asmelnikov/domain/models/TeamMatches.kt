package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMatches(
    val id: String = UUID.randomUUID().toString(),
    val matchesCompleted: List<Match> = emptyList(),
    val matchesAhead: List<Match> = emptyList(),
) : Parcelable
