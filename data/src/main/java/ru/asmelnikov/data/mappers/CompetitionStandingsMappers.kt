package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.StandingDTO
import ru.asmelnikov.data.models.TableDTO
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Group
import ru.asmelnikov.domain.models.Stage
import ru.asmelnikov.domain.models.Standing
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.domain.models.TournamentType
import java.util.UUID

fun CompetitionStandingsModelDTO.toCompetitionStandingsEntity(): CompetitionStandingsEntity {
    return CompetitionStandingsEntity().apply {
        id = this@toCompetitionStandingsEntity.competition?.id?.toString() ?: UUID.randomUUID()
            .toString()
        area = this@toCompetitionStandingsEntity.area.toAreaEntity()
        competition = this@toCompetitionStandingsEntity.competition?.toCompetitionEmbeddedEntity()
        standings = realmListOf<StandingEntity>().apply {
            this@toCompetitionStandingsEntity.standings?.map { it.toStandingEntity() }
                ?.let { addAll(it) }
        }
    }
}

fun StandingDTO.toStandingEntity(): StandingEntity {
    return StandingEntity().apply {
        group = this@toStandingEntity.group ?: ""
        stage = this@toStandingEntity.stage ?: ""
        type = this@toStandingEntity.type ?: ""
        table = realmListOf<TableEntity>().apply {
            this@toStandingEntity.table?.map { it.toTableEntity() }?.let { addAll(it) }
        }
    }
}

fun TableDTO?.toTableEntity(): TableEntity {
    return TableEntity().apply {
        draw = this@toTableEntity?.draw ?: 0
        form = this@toTableEntity?.form ?: ""
        goalDifference = this@toTableEntity?.goalDifference ?: 0
        goalsAgainst = this@toTableEntity?.goalsAgainst ?: 0
        goalsFor = this@toTableEntity?.goalsFor ?: 0
        lost = this@toTableEntity?.lost ?: 0
        playedGames = this@toTableEntity?.playedGames ?: 0
        points = this@toTableEntity?.points ?: 0
        position = this@toTableEntity?.position ?: 0
        team = this@toTableEntity?.team?.toTeamEmbeddedEntity()
        won = this@toTableEntity?.won ?: 0
    }
}

fun CompetitionStandingsEntity?.toCompetitionStandings(): CompetitionStandings {
    return CompetitionStandings(
        id = this?.competition?.id?.toString() ?: UUID.randomUUID().toString(),
        area = this?.area.toArea(),
        competition = this?.competition.toCompetition(),
        standings = this?.standings?.map { it.toStanding() } ?: emptyList()
    )
}


fun CompetitionEmbeddedEntity?.toCompetition(): Competition {
    return Competition(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        area = this?.area.toArea(),
        code = this?.code ?: "",
        currentSeason = this?.currentSeason.toCurrentSeason(),
        emblem = this?.emblem ?: "",
        name = this?.name ?: ""
    )
}

fun StandingEntity.toStanding(): Standing {
    return Standing(
        group = Group.safeValueOf(group),
        stage = Stage.safeValueOf(stage),
        type = TournamentType.safeValueOf(type),
        table = this.table?.map { it.toTable() }?.sortedBy { it.position } ?: emptyList()
    )
}

fun TableEntity.toTable(): Table {
    return Table(
        draw = draw,
        form = form,
        goalDifference = goalDifference,
        goalsAgainst = goalsAgainst,
        goalsFor = goalsFor,
        lost = lost,
        playedGames = playedGames,
        points = points,
        position = position,
        team = this.team.toTeam(),
        won = won
    )
}