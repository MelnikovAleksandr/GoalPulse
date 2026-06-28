package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.AggregatesDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.TeamH2HDTO
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.TeamH2H

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
        numberOfMatches = this?.numberOfMatches ?: 0,
        totalGoals = this?.totalGoals ?: 0,
        homeWinsPercentage = homeWinsPercentage ?: 0f,
        awayWinsPercentage = awayWinsPercentage ?: 0f,
        drawsPercentage = drawsPercentage ?: 0f,
        awayTeam = this?.awayTeam.toTeamH2H(),
        homeTeam = this?.homeTeam.toTeamH2H()
    )
}


fun TeamH2HDTO?.toTeamH2H(): TeamH2H {
    return TeamH2H(
        draws = this?.draws ?: 0,
        id = this?.id ?: 0,
        losses = this?.losses ?: 0,
        name = this?.name ?: "",
        wins = this?.wins ?: 0
    )
}