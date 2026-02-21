package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import ru.asmelnikov.data.local.models.PersonEntity
import ru.asmelnikov.data.local.models.ContractEntity
import ru.asmelnikov.data.local.models.SquadByPositionEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.models.PersonDTO
import ru.asmelnikov.data.models.ContractDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.domain.models.Coach
import ru.asmelnikov.domain.models.Contract
import ru.asmelnikov.domain.models.Squad
import ru.asmelnikov.domain.models.SquadByPosition
import ru.asmelnikov.domain.models.TeamInfo
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.UUID

fun TeamInfoDTO.toTeamInfoEntity(): TeamInfoEntity {
    return TeamInfoEntity().apply {
        id = this@toTeamInfoEntity.id?.toString() ?: UUID.randomUUID().toString()
        address = this@toTeamInfoEntity.address ?: ""
        area = this@toTeamInfoEntity.area.toAreaEntity()
        clubColors = this@toTeamInfoEntity.clubColors ?: ""
        coach = this@toTeamInfoEntity.coach.toCoachEntity()
        crest = this@toTeamInfoEntity.crest ?: ""
        founded = this@toTeamInfoEntity.founded ?: -1
        name = this@toTeamInfoEntity.name ?: ""
        shortName = this@toTeamInfoEntity.shortName ?: ""
        squadByPosition = convertToRealmList(this@toTeamInfoEntity.squad)
        tla = this@toTeamInfoEntity.tla ?: ""
        venue = this@toTeamInfoEntity.venue ?: ""
        website = this@toTeamInfoEntity.website ?: ""
    }
}

fun PersonDTO?.toCoachEntity(): PersonEntity {
    return PersonEntity().apply {
        id = this@toCoachEntity?.id ?: UUID.randomUUID().hashCode()
        contract = this@toCoachEntity?.contract.toContractEntity()
        dateOfBirth = this@toCoachEntity?.dateOfBirth ?: ""
        firstName = this@toCoachEntity?.firstName ?: ""
        lastName = this@toCoachEntity?.lastName ?: ""
        name = this@toCoachEntity?.name ?: ""
        nationality = this@toCoachEntity?.nationality ?: ""
    }
}

fun ContractDTO?.toContractEntity(): ContractEntity {
    return ContractEntity().apply {
        start = this@toContractEntity?.start ?: ""
        until = this@toContractEntity?.until ?: ""
    }
}

fun TeamInfoEntity.toTeamInfo(): TeamInfo {
    return TeamInfo(
        address = address,
        area = area.toArea(),
        clubColors = clubColors,
        coach = coach.toCoach(),
        crest = crest,
        founded = founded,
        id = id,
        name = name,
        shortName = shortName,
        squadByPosition = squadByPosition?.map { it.toSquadByPosition() } ?: emptyList(),
        tla = tla,
        venue = venue,
        website = website
    )
}

fun PersonEntity?.toCoach(): Coach {
    return Coach(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        contract = this?.contract.toContract(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        lastName = this?.lastName ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: ""
    )
}

fun ContractEntity?.toContract(): Contract {
    return Contract(
        start = this?.start ?: "",
        until = this?.until ?: ""
    )
}

fun SquadByPositionEntity.toSquadByPosition(): SquadByPosition {
    return SquadByPosition(
        position = position,
        squad = squad?.map { it.toSquad() } ?: emptyList()
    )
}

fun PersonEntity.toSquad(): Squad {
    return Squad(
        age = dateOfBirth.calculateAge(),
        id = id,
        name = name,
        nationality = nationality
    )
}

fun convertToRealmList(squadDTOList: List<PersonDTO>?): RealmList<SquadByPositionEntity> {
    val squadByPositionList = realmListOf<SquadByPositionEntity>()
    squadDTOList?.groupBy { it.position }?.forEach { (position, squadDTOs) ->
        val squadEntityList = realmListOf<PersonEntity>()
        squadDTOs.forEach { squadDTO ->
            val squadEntity = PersonEntity().apply {
                dateOfBirth = squadDTO.dateOfBirth ?: ""
                id = squadDTO.id ?: -1
                name = squadDTO.name ?: ""
                nationality = squadDTO.nationality ?: ""
            }
            squadEntityList.add(squadEntity)
        }
        val squadByPositionEntity = SquadByPositionEntity().apply {
            this.position = position ?: ""
            this.squad = squadEntityList
        }
        squadByPositionList.add(squadByPositionEntity)
    }
    return squadByPositionList
}

fun String.calculateAge(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return try {
        val dob = LocalDate.parse(this, formatter)
        val currentDate = LocalDate.now()
        val age = Period.between(dob, currentDate).years
        age.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
