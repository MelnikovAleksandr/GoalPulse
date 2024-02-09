package ru.asmelnikov.data.mappers

import io.realm.RealmList
import ru.asmelnikov.data.local.models.CoachEntity
import ru.asmelnikov.data.local.models.ContractEntity
import ru.asmelnikov.data.local.models.SquadByPositionEntity
import ru.asmelnikov.data.local.models.SquadEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.models.CoachDTO
import ru.asmelnikov.data.models.ContractDTO
import ru.asmelnikov.data.models.SquadDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.domain.models.Coach
import ru.asmelnikov.domain.models.Contract
import ru.asmelnikov.domain.models.Squad
import ru.asmelnikov.domain.models.SquadByPosition
import ru.asmelnikov.domain.models.TeamInfo

fun TeamInfoDTO.toTeamInfoEntity(): TeamInfoEntity {
    return TeamInfoEntity(
        address = address ?: "",
        area = area.toAreaEntity(),
        clubColors = clubColors ?: "",
        coach = coach.toCoachEntity(),
        crest = crest ?: "",
        founded = founded ?: -1,
        id = id.toString(),
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        shortName = shortName ?: "",
        squadByPosition = convertToRealmList(squad),
        tla = tla ?: "",
        venue = venue ?: "",
        website = website ?: ""
    )
}

fun CoachDTO?.toCoachEntity(): CoachEntity {
    return CoachEntity(
        contract = this?.contract.toContractEntity(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        id = this?.id ?: -1,
        lastName = this?.lastName ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: ""
    )
}

fun ContractDTO?.toContractEntity(): ContractEntity {
    return ContractEntity(
        start = this?.start ?: "",
        until = this?.until ?: ""
    )
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
        lastUpdated = lastUpdated,
        name = name,
        shortName = shortName,
        squadByPosition = squadByPosition?.map { it.toSquadByPosition() } ?: emptyList(),
        tla = tla,
        venue = venue,
        website = website
    )
}

fun CoachEntity?.toCoach(): Coach {
    return Coach(
        contract = this?.contract.toContract(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        id = this?.id ?: -1,
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

fun SquadEntity.toSquad(): Squad {
    return Squad(
        dateOfBirth = dateOfBirth,
        id = id,
        name = name,
        nationality = nationality
    )
}

fun convertToRealmList(squadDTOList: List<SquadDTO>?): RealmList<SquadByPositionEntity> {
    val squadByPositionList = RealmList<SquadByPositionEntity>()
    squadDTOList?.groupBy { it.position }?.forEach { (position, squadDTOs) ->
        val squadEntityList = RealmList<SquadEntity>()
        squadDTOs.forEach { squadDTO ->
            val squadEntity = SquadEntity(
                squadDTO.dateOfBirth ?: "",
                squadDTO.id ?: -1,
                squadDTO.name ?: "",
                squadDTO.nationality ?: ""
            )
            squadEntityList.add(squadEntity)
        }
        val squadByPositionEntity = SquadByPositionEntity(position ?: "", squadEntityList)
        squadByPositionList.add(squadByPositionEntity)
    }
    return squadByPositionList
}