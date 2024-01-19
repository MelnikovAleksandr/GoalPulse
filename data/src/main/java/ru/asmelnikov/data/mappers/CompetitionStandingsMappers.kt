package ru.asmelnikov.data.mappers

import io.realm.RealmList
import ru.asmelnikov.data.local.models.*
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.FiltersDTO
import ru.asmelnikov.data.models.SeasonDTO
import ru.asmelnikov.data.models.StandingDTO
import ru.asmelnikov.data.models.TableDTO
import ru.asmelnikov.data.models.TeamDTO
import ru.asmelnikov.domain.models.*


fun CompetitionStandingsModelDTO.toCompetitionStandingsEntity(): CompetitionStandingsEntity {
    val standings: RealmList<StandingEntity> = RealmList()
    this@toCompetitionStandingsEntity.standings?.map { it.toStandingEntity() }
        ?.let { standings.addAll(it) }
    return CompetitionStandingsEntity(
        id = this.competition?.id.toString(),
        area = this.area.toAreaEntity(),
        competition = this.competition?.toCompetitionEmbeddedEntity(),
        filters = this.filters?.toFiltersEntity(),
        season = this.season?.toSeasonEntity(),
        standings = standings
    )
}

fun CompetitionDTO.toCompetitionEmbeddedEntity(): CompetitionEmbeddedEntity {
    return CompetitionEmbeddedEntity(
        area = area.toAreaEntity(),
        code = code ?: "",
        currentSeason = currentSeason.toCurrentSeasonEntity(),
        emblem = emblem ?: "",
        id = id ?: -1,
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        numberOfAvailableSeasons = numberOfAvailableSeasons ?: -1,
        plan = plan ?: "",
        type = type ?: ""
    )
}

fun FiltersDTO.toFiltersEntity(): FiltersEntity {
    return FiltersEntity(
        season = season ?: ""
    )
}

fun SeasonDTO.toSeasonEntity(): SeasonEntity {
    return SeasonEntity(
        currentMatchday = currentMatchday ?: -1,
        endDate = endDate ?: "",
        id = id ?: -1,
        startDate = startDate ?: "",
        winner = winner.toWinnerEntity()
    )
}

fun StandingDTO.toStandingEntity(): StandingEntity {
    val table: RealmList<TableEntity> = RealmList()
    this@toStandingEntity.table?.map { it.toTableEntity() }?.let { table.addAll(it) }
    return StandingEntity(
        group = group ?: "",
        stage = stage ?: "",
        type = type ?: "",
        table = table
    )
}

fun TableDTO?.toTableEntity(): TableEntity {
    return TableEntity(
        draw = this?.draw ?: -1,
        form = this?.form ?: "",
        goalDifference = this?.goalDifference ?: -1,
        goalsAgainst = this?.goalsAgainst ?: -1,
        goalsFor = this?.goalsFor ?: -1,
        lost = this?.lost ?: -1,
        playedGames = this?.playedGames ?: -1,
        points = this?.points ?: -1,
        position = this?.position ?: -1,
        team = this?.team?.toTeamEntity(),
        won = this?.won ?: -1
    )
}

fun TeamDTO.toTeamEntity(): TeamEntity {
    return TeamEntity(
        crest = crest ?: "",
        id = id ?: -1,
        name = name ?: "",
        shortName = shortName ?: "",
        tla = tla ?: ""
    )
}

fun CompetitionStandingsEntity?.toCompetitionStandings(): CompetitionStandings {
    return CompetitionStandings(
        id = this?.competition?.id.toString(),
        area = this?.area.toArea(),
        competition = this?.competition.toCompetition(),
        filters = this?.filters.toFilters(),
        season = this?.season.toSeason(),
        standings = this?.standings?.map { it.toStanding() } ?: emptyList()
    )
}

fun CompetitionEmbeddedEntity?.toCompetition(): Competition {
    return Competition(
        area = this?.area.toArea(),
        code = this?.code ?: "",
        currentSeason = this?.currentSeason.toCurrentSeason(),
        emblem = this?.emblem ?: "",
        id = this?.id ?: -1,
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        numberOfAvailableSeasons = this?.numberOfAvailableSeasons ?: -1,
        plan = this?.plan ?: "",
        type = this?.type ?: ""
    )
}

fun FiltersEntity?.toFilters(): Filters {
    return Filters(
        season = this?.season ?: ""
    )
}

fun SeasonEntity?.toSeason(): Season {
    return Season(
        currentMatchday = this?.currentMatchday ?: -1,
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinner()
    )
}

fun StandingEntity.toStanding(): Standing {
    return Standing(
        group = group,
        stage = stage,
        type = type,
        table = this.table?.map { it.toTable() } ?: emptyList()
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

fun TeamEntity?.toTeam(): Team {
    return Team(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}