package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.SeasonEntity
import ru.asmelnikov.data.local.models.WinnerEntity
import ru.asmelnikov.data.models.AreaDTO
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CurrentSeasonDTO
import ru.asmelnikov.data.models.WinnerDTO
import ru.asmelnikov.domain.models.Area
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CurrentSeason
import ru.asmelnikov.domain.models.Winner

fun CompetitionDTO.toCompetitionEntity(): CompetitionEntity {
    val seasons: RealmList<SeasonEntity> = realmListOf()
    this@toCompetitionEntity.seasons?.map {
        it.toSeasonEntity()
    }?.take(4)?.let { seasons.addAll(it) }
    return CompetitionEntity(
        area = area.toAreaEntity(),
        code = code ?: "",
        currentSeason = currentSeason.toCurrentSeasonEntity(),
        emblem = emblem ?: "",
        id = id ?: -1,
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        numberOfAvailableSeasons = numberOfAvailableSeasons ?: -1,
        plan = plan ?: "",
        type = type ?: "",
        seasons = seasons
    )
}

fun AreaDTO?.toAreaEntity(): AreaEntity {
    return AreaEntity(
        code = this?.code ?: "",
        flag = this?.flag ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: ""
    )
}

fun CurrentSeasonDTO?.toCurrentSeasonEntity(): CurrentSeasonEntity {
    return CurrentSeasonEntity(
        currentMatchDay = this?.currentMatchDay ?: -1,
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinnerEntity()
    )
}

fun WinnerDTO?.toWinnerEntity(): WinnerEntity {
    return WinnerEntity(
        address = this?.address ?: "",
        clubColors = this?.clubColors ?: "",
        crest = this?.crest ?: "",
        founded = this?.founded ?: -1,
        id = this?.id ?: -1,
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: "",
        website = this?.website ?: "",
        venue = this?.venue ?: ""
    )
}

fun CompetitionEntity?.toCompetition(): Competition {
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
        type = this?.type ?: "",
        seasons = this?.seasons?.map { it.toSeason() } ?: emptyList()
    )
}

fun AreaEntity?.toArea(): Area {
    return Area(
        code = this?.code ?: "",
        flag = this?.flag ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: ""
    )
}

fun CurrentSeasonEntity?.toCurrentSeason(): CurrentSeason {
    return CurrentSeason(
        currentMatchDay = this?.currentMatchDay ?: -1,
        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinner()
    )
}

fun WinnerEntity?.toWinner(): Winner {
    return Winner(
        address = this?.address ?: "",
        clubColors = this?.clubColors ?: "",
        crest = this?.crest ?: "",
        founded = this?.founded ?: -1,
        id = this?.id ?: -1,
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: "",
        website = this?.website ?: "",
        venue = this?.venue ?: ""
    )
}