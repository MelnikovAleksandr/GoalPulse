package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamInfo(
    val address: String = "",
    val area: Area = Area(),
    val clubColors: String = "",
    val coach: Coach = Coach(),
    val crest: String = "",
    val founded: Int = -1,
    val id: String = "",
    val name: String = "",
    val shortName: String = "",
    val squadByPosition: List<SquadByPosition> = emptyList(),
    val tla: String = "",
    val venue: String = "",
    val website: String = ""
) : Parcelable

@Parcelize
data class Coach(
    val contract: Contract = Contract(),
    val dateOfBirth: String = "",
    val firstName: String = "",
    val id: Int = -1,
    val lastName: String = "",
    val name: String = "",
    val nationality: String = ""
) : Parcelable

@Parcelize
data class SquadByPosition(
    val position: String = "",
    val squad: List<Squad> = emptyList()
) : Parcelable

@Parcelize
data class Squad(
    val age: String = "",
    val id: Int = -1,
    val name: String = "",
    val nationality: String = ""
) : Parcelable

@Parcelize
data class Contract(
    val start: String = "",
    val until: String = ""
) : Parcelable
