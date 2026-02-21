package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Competition(
    val area: Area,
    val code: String,
    val currentSeason: CurrentSeason,
    val emblem: String,
    val id: Int,
    val name: String,
    val type: String
) : Parcelable

@Parcelize
data class Area(
    val code: String = "",
    val flag: String = "",
    val id: Int = -1,
    val name: String = ""
) : Parcelable

@Parcelize
data class CurrentSeason(
    val currentMatchDay: Int,
    val startDateEndDate: String, // example - 2022/2023
    val endDate: String,
    val id: Int,
    val startDate: String,
    val winner: Team
) : Parcelable