package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.MatchDTO
import ru.asmelnikov.data.models.TeamH2HDTO
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.TeamH2H

fun Head2headDTO.toHead2head(id: Int): Head2head {
    return Head2head(
        id = id,
        aggregates = calculateAggregatesFromMatches()
    )
}

private fun Head2headDTO.calculateAggregatesFromMatches(): Aggregates {
    val homeTeamDto = aggregates?.homeTeam
    val awayTeamDto = aggregates?.awayTeam
    val homeTeamId = homeTeamDto?.id
    val awayTeamId = awayTeamDto?.id

    if (homeTeamId == null || awayTeamId == null) {
        return emptyAggregates(homeTeamDto, awayTeamDto)
    }

    var homeWins = 0
    var homeDraws = 0
    var homeLosses = 0
    var totalGoals = 0

    matches.orEmpty()
        .filter { it.status == "FINISHED" }
        .forEach { match ->
            val matchHomeTeamId = match.homeTeam?.id
            val matchAwayTeamId = match.awayTeam?.id
            val homeGoals = match.score?.fullTime?.home
            val awayGoals = match.score?.fullTime?.away

            if (homeGoals == null || awayGoals == null) return@forEach

            val isHomeTeamMatch = matchHomeTeamId == homeTeamId && matchAwayTeamId == awayTeamId
            val isAwayTeamMatch = matchHomeTeamId == awayTeamId && matchAwayTeamId == homeTeamId
            if (!isHomeTeamMatch && !isAwayTeamMatch) return@forEach

            totalGoals += homeGoals + awayGoals

            val h2hHomeGoals = if (isHomeTeamMatch) homeGoals else awayGoals
            val h2hAwayGoals = if (isHomeTeamMatch) awayGoals else homeGoals

            when {
                h2hHomeGoals > h2hAwayGoals -> homeWins++
                h2hHomeGoals < h2hAwayGoals -> homeLosses++
                else -> homeDraws++
            }
        }

    val numberOfMatches = homeWins + homeDraws + homeLosses
    val (homeWinsPercentage, drawsPercentage, awayWinsPercentage) =
        calculatePercentages(homeWins, homeDraws, homeLosses)

    return Aggregates(
        numberOfMatches = numberOfMatches,
        totalGoals = totalGoals,
        homeWinsPercentage = homeWinsPercentage,
        awayWinsPercentage = awayWinsPercentage,
        drawsPercentage = drawsPercentage,
        homeTeam = TeamH2H(
            id = homeTeamId,
            name = homeTeamDto.name.orEmpty(),
            wins = homeWins,
            draws = homeDraws,
            losses = homeLosses
        ),
        awayTeam = TeamH2H(
            id = awayTeamId,
            name = awayTeamDto.name.orEmpty(),
            wins = homeLosses,
            draws = homeDraws,
            losses = homeWins
        )
    )
}

private fun calculatePercentages(
    homeWins: Int,
    homeDraws: Int,
    homeLosses: Int
): Triple<Float, Float, Float> {
    val total = homeWins + homeDraws + homeLosses
    if (total == 0) return Triple(0f, 0f, 0f)

    val counts = listOf(homeWins, homeDraws, homeLosses)
    val exact = counts.map { it.toFloat() / total * 100 }
    val rounded = exact.map { it.toInt() }.toMutableList()
    var remaining = 100 - rounded.sum()
    val remainders = exact
        .mapIndexed { index, value -> index to (value - value.toInt()) }
        .sortedByDescending { it.second }

    for (i in 0 until remaining) {
        rounded[remainders[i].first]++
    }

    return Triple(
        rounded[0].toFloat(),
        rounded[1].toFloat(),
        rounded[2].toFloat()
    )
}

private fun emptyAggregates(
    homeTeamDto: TeamH2HDTO?,
    awayTeamDto: TeamH2HDTO?
): Aggregates {
    return Aggregates(
        numberOfMatches = 0,
        totalGoals = 0,
        homeWinsPercentage = 0f,
        awayWinsPercentage = 0f,
        drawsPercentage = 0f,
        homeTeam = TeamH2H(
            id = homeTeamDto?.id ?: 0,
            name = homeTeamDto?.name.orEmpty(),
            wins = 0,
            draws = 0,
            losses = 0
        ),
        awayTeam = TeamH2H(
            id = awayTeamDto?.id ?: 0,
            name = awayTeamDto?.name.orEmpty(),
            wins = 0,
            draws = 0,
            losses = 0
        )
    )
}
