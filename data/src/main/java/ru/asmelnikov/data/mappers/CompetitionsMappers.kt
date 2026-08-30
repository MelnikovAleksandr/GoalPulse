package ru.asmelnikov.data.mappers

import java.util.UUID
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

fun CompetitionDTO.toCompetitionEntity(): CompetitionEntity = CompetitionEntity().apply {
    id = this@toCompetitionEntity.id ?: UUID.randomUUID().hashCode()
    area = this@toCompetitionEntity.area.toAreaEntity()
    code = this@toCompetitionEntity.code ?: ""
    currentSeason = this@toCompetitionEntity.currentSeason.toCurrentSeasonEntity()
    emblem = this@toCompetitionEntity.emblem ?: ""
    name = this@toCompetitionEntity.name ?: ""
    type = this@toCompetitionEntity.type ?: ""
}

fun CompetitionDTO.toCompetitionEmbeddedEntity(): CompetitionEmbeddedEntity = CompetitionEmbeddedEntity().apply {
    id = this@toCompetitionEmbeddedEntity.id ?: UUID.randomUUID().hashCode()
    area = this@toCompetitionEmbeddedEntity.area.toAreaEntity()
    code = this@toCompetitionEmbeddedEntity.code ?: ""
    currentSeason = this@toCompetitionEmbeddedEntity.currentSeason.toCurrentSeasonEntity()
    emblem = this@toCompetitionEmbeddedEntity.emblem ?: ""
    name = this@toCompetitionEmbeddedEntity.name ?: ""
    type = this@toCompetitionEmbeddedEntity.type ?: ""
}

fun AreaDTO?.toAreaEntity(): AreaEntity = AreaEntity().apply {
    id = this@toAreaEntity?.id ?: UUID.randomUUID().hashCode()
    code = this@toAreaEntity?.code ?: ""
    flag = this@toAreaEntity?.flag ?: ""
    name = this@toAreaEntity?.name ?: ""
}

fun CurrentSeasonDTO?.toCurrentSeasonEntity(): CurrentSeasonEntity = CurrentSeasonEntity().apply {
    id = this@toCurrentSeasonEntity?.id ?: UUID.randomUUID().hashCode()
    currentMatchDay = this@toCurrentSeasonEntity?.currentMatchDay ?: 0
    endDate = this@toCurrentSeasonEntity?.endDate ?: ""
    startDate = this@toCurrentSeasonEntity?.startDate ?: ""
    winner = this@toCurrentSeasonEntity?.winner?.toTeamEmbeddedEntity()
}

fun CompetitionEntity?.toCompetition(): Competition = Competition(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    area = this?.area.toArea(),
    code = this?.code ?: "",
    currentSeason = this?.currentSeason.toCurrentSeason(),
    emblem = this?.emblem ?: "",
    name = this?.name ?: "",
)

fun AreaEntity?.toArea(): Area = Area(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    flag = this?.flag ?: "",
    name = this?.name ?: "",
)

fun CurrentSeasonEntity?.toCurrentSeason(): CurrentSeason = CurrentSeason(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    currentMatchDay = this?.currentMatchDay ?: 0,
    startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
    endDate = this?.endDate ?: "",
    startDate = this?.startDate ?: "",
)

fun TeamEmbeddedEntity?.toTeam(): Team = Team(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    crest = this?.crest ?: "",
    name = this?.name ?: "",
    shortName = this?.shortName ?: "",
    tla = this?.tla ?: "",
)
