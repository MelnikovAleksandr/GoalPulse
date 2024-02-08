package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.AggregatesDTO
import ru.asmelnikov.data.models.AwayTeamH2HDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.HomeTeamH2HDTO
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.AwayTeamH2H
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.HomeTeamH2H

fun Head2headDTO.toHead2head(id: Int): Head2head {
    return Head2head(
        id = id,
        aggregates = aggregates.toAggregates()
    )
}

fun AggregatesDTO?.toAggregates(): Aggregates {
    val totalMatches = this?.numberOfMatches?.toFloat() ?: 0f
    val homeWinsPercentage = if (totalMatches > 0) {
        (this?.homeTeam?.wins?.toFloat()?.div(totalMatches))?.times(100)
    } else {
        0f
    }
    val awayWinsPercentage = if (totalMatches > 0) {
        (this?.awayTeam?.wins?.toFloat()?.div(totalMatches))?.times(100)
    } else {
        0f
    }
    val drawsPercentage = if (totalMatches > 0) {
        (this?.homeTeam?.draws?.toFloat()?.div(totalMatches))?.times(100)
    } else {
        0f
    }

    return Aggregates(
        numberOfMatches = this?.numberOfMatches ?: -1,
        totalGoals = this?.totalGoals ?: -1,
        homeWinsPercentage = homeWinsPercentage ?: -1f,
        awayWinsPercentage = awayWinsPercentage ?: -1f,
        drawsPercentage = drawsPercentage ?: -1f,
        awayTeam = this?.awayTeam.toAwayTeamH2H(),
        homeTeam = this?.homeTeam.toHomeTeamH2H()
    )
}

fun AwayTeamH2HDTO?.toAwayTeamH2H(): AwayTeamH2H {
    return AwayTeamH2H(
        draws = this?.draws ?: -1,
        id = this?.id ?: -1,
        losses = this?.losses ?: -1,
        name = this?.name ?: "",
        wins = this?.wins ?: -1
    )
}

fun HomeTeamH2HDTO?.toHomeTeamH2H(): HomeTeamH2H {
    return HomeTeamH2H(
        draws = this?.draws ?: -1,
        id = this?.id ?: -1,
        losses = this?.losses ?: -1,
        name = this?.name ?: "",
        wins = this?.wins ?: -1
    )
}