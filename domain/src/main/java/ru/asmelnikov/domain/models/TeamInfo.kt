package ru.asmelnikov.domain.models

import android.os.Parcelable
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamInfo(
    val id: String = UUID.randomUUID().toString(),
    val address: String = "",
    val area: Area = Area(),
    val clubColors: String = "",
    val coach: Coach = Coach(),
    val crest: String = "",
    val founded: Int = -1,
    val name: String = "",
    val shortName: String = "",
    val squadByPosition: List<SquadByPosition> = emptyList(),
    val tla: String = "",
    val venue: String = "",
    val website: String = "",
) : Parcelable

@Parcelize
data class Coach(
    val id: Int = UUID.randomUUID().hashCode(),
    val contract: Contract = Contract(),
    val dateOfBirth: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "",
    val nationality: String = "",
) : Parcelable

@Parcelize
data class SquadByPosition(val position: PlayerPosition = PlayerPosition.NON, val squad: List<Squad> = emptyList()) :
    Parcelable

@Parcelize
data class Squad(
    val id: Int = UUID.randomUUID().hashCode(),
    val age: String = "",
    val name: String = "",
    val nationality: String = "",
) : Parcelable

@Parcelize
data class Contract(val start: String = "", val until: String = "") : Parcelable
