package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Competition(
    val id: Int = UUID.randomUUID().hashCode(),
    val area: Area = Area(),
    val code: String = "",
    val currentSeason: CurrentSeason = CurrentSeason(),
    val emblem: String = "",
    val name: String = "",
) : Parcelable

@Parcelize
data class Area(val id: Int = UUID.randomUUID().hashCode(), val flag: String = "", val name: String = "") : Parcelable

@Parcelize
data class CurrentSeason(
    val id: Int = UUID.randomUUID().hashCode(),
    val currentMatchDay: Int = -1,
    val startDateEndDate: String = "", // example - 2022/2023
    val endDate: String = "",
    val startDate: String = "",
) : Parcelable
