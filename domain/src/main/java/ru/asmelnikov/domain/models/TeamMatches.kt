package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class TeamMatches(
    val id: String = UUID.randomUUID().toString(),
    val matchesCompleted: List<Match> = emptyList(),
    val matchesAhead: List<Match> = emptyList()
) : Parcelable