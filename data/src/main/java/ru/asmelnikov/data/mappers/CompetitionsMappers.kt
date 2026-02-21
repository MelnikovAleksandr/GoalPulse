package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.models.AreaDTO
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CurrentSeasonDTO
import ru.asmelnikov.domain.models.Area
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CurrentSeason
import ru.asmelnikov.domain.models.Team
import java.util.UUID

fun CompetitionDTO.toCompetitionEntity(): CompetitionEntity {
    return CompetitionEntity().apply {
        id = this@toCompetitionEntity.id ?: UUID.randomUUID().hashCode()
        area = this@toCompetitionEntity.area.toAreaEntity()
        code = this@toCompetitionEntity.code ?: ""
        currentSeason = this@toCompetitionEntity.currentSeason.toCurrentSeasonEntity()
        emblem = this@toCompetitionEntity.emblem ?: ""
        name = this@toCompetitionEntity.name ?: ""
        type = this@toCompetitionEntity.type ?: ""
    }
}

fun CompetitionDTO.toCompetitionEmbeddedEntity(): CompetitionEmbeddedEntity {
    return CompetitionEmbeddedEntity().apply {
        id = this@toCompetitionEmbeddedEntity.id ?: UUID.randomUUID().hashCode()
        area = this@toCompetitionEmbeddedEntity.area.toAreaEntity()
        code = this@toCompetitionEmbeddedEntity.code ?: ""
        currentSeason = this@toCompetitionEmbeddedEntity.currentSeason.toCurrentSeasonEntity()
        emblem = this@toCompetitionEmbeddedEntity.emblem ?: ""
        name = this@toCompetitionEmbeddedEntity.name ?: ""
        type = this@toCompetitionEmbeddedEntity.type ?: ""
    }
}

fun AreaDTO?.toAreaEntity(): AreaEntity {
    return AreaEntity().apply {
        id = this@toAreaEntity?.id ?: UUID.randomUUID().hashCode()
        code = this@toAreaEntity?.code ?: ""
        flag = this@toAreaEntity?.flag ?: ""
        name = this@toAreaEntity?.name ?: ""
    }
}

fun CurrentSeasonDTO?.toCurrentSeasonEntity(): CurrentSeasonEntity {
    return CurrentSeasonEntity().apply {
        id = this@toCurrentSeasonEntity?.id ?: UUID.randomUUID().hashCode()
        currentMatchDay = this@toCurrentSeasonEntity?.currentMatchDay ?: -1
        endDate = this@toCurrentSeasonEntity?.endDate ?: ""
        startDate = this@toCurrentSeasonEntity?.startDate ?: ""
        winner = this@toCurrentSeasonEntity?.winner?.toTeamEmbeddedEntity()
    }
}

fun CompetitionEntity?.toCompetition(): Competition {
    return Competition(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        area = this?.area.toArea(),
        code = this?.code ?: "",
        currentSeason = this?.currentSeason.toCurrentSeason(),
        emblem = this?.emblem ?: "",
        name = this?.name ?: "",
        type = this?.type ?: ""
    )
}

fun AreaEntity?.toArea(): Area {
    return Area(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        code = this?.code ?: "",
        flag = this?.flag ?: "",
        name = this?.name ?: ""
    )
}

fun CurrentSeasonEntity?.toCurrentSeason(): CurrentSeason {
    return CurrentSeason(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        currentMatchDay = this?.currentMatchDay ?: -1,
        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
        endDate = this?.endDate ?: "",
        startDate = this?.startDate ?: "",
        winner = this?.winner.toTeam()
    )
}

fun TeamEmbeddedEntity?.toTeam(): Team {
    return Team(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        crest = this?.crest ?: "",
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: "",
    )
}