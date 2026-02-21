package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val currentTeam: CurrentTeam = CurrentTeam(),
    val age: String = "",
    val firstName: String = "",
    val id: Int = -1,
    val lastName: String = "",
    val lastUpdated: String = "",
    val name: String = "",
    val nationality: String = "",
    val position: String = "",
    val section: String = "",
    val shirtNumber: Int = -1
) : Parcelable

@Parcelize
data class CurrentTeam(
    val address: String = "",
    val area: Area = Area(),
    val clubColors: String = "",
    val crest: String = "",
    val founded: Int = -1,
    val id: Int = -1,
    val name: String = "",
    val shortName: String = "",
    val tla: String = "",
    val venue: String = "",
    val website: String = ""
) : Parcelable
