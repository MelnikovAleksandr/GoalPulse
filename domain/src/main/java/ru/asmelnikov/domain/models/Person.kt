package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Int = UUID.randomUUID().hashCode(),
    val currentTeam: CurrentTeam = CurrentTeam(),
    val age: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val lastUpdated: String = "",
    val name: String = "",
    val nationality: String = "",
    val position: PlayerPosition = PlayerPosition.NON,
    val section: String = "",
    val shirtNumber: Int = -1,
) : Parcelable

@Parcelize
data class CurrentTeam(
    val id: Int = UUID.randomUUID().hashCode(),
    val address: String = "",
    val area: Area = Area(),
    val clubColors: String = "",
    val crest: String = "",
    val founded: Int = -1,
    val name: String = "",
    val shortName: String = "",
    val tla: String = "",
    val venue: String = "",
    val website: String = "",
) : Parcelable
