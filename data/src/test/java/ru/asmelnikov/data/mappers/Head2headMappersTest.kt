package ru.asmelnikov.data.mappers

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.models.AggregatesDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.MatchDTO
import ru.asmelnikov.data.models.ScoreDTO
import ru.asmelnikov.data.models.TeamH2HDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.data.models.TimeDTO
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.TeamH2H

class Head2headMappersTest {

    @Test
    fun toHead2head_recalculatesWinsDrawsLossesFromFinishedMatches() {
        val dto = head2headDto(
            homeId = 57,
            homeName = "Arsenal",
            awayId = 61,
            awayName = "Chelsea",
            matches = listOf(
                match(homeId = 57, awayId = 61, homeGoals = 2, awayGoals = 1, status = "FINISHED"),
                match(homeId = 61, awayId = 57, homeGoals = 1, awayGoals = 1, status = "FINISHED"),
                match(homeId = 61, awayId = 57, homeGoals = 2, awayGoals = 0, status = "FINISHED"),
                match(homeId = 57, awayId = 61, homeGoals = 3, awayGoals = 0, status = "TIMED"),
                match(homeId = 57, awayId = 61, homeGoals = null, awayGoals = null, status = "FINISHED"),
                match(homeId = 57, awayId = 64, homeGoals = 4, awayGoals = 0, status = "FINISHED"),
            ),
        )

        assertEquals(
            Aggregates(
                homeTeam = TeamH2H(id = 57, name = "Arsenal", wins = 1, draws = 1, losses = 1),
                awayTeam = TeamH2H(id = 61, name = "Chelsea", wins = 1, draws = 1, losses = 1),
                homeWinsPercentage = 34f,
                drawsPercentage = 33f,
                awayWinsPercentage = 33f,
                numberOfMatches = 3,
                totalGoals = 7,
            ),
            dto.toHead2head(id = 1).aggregates,
        )
    }

    @Test
    fun toHead2head_roundsPercentagesSoTheySumTo100() {
        val dto = head2headDto(
            homeId = 81,
            homeName = "Barcelona",
            awayId = 86,
            awayName = "Real Madrid",
            matches = listOf(
                match(homeId = 81, awayId = 86, homeGoals = 2, awayGoals = 0, status = "FINISHED"),
                match(homeId = 81, awayId = 86, homeGoals = 1, awayGoals = 0, status = "FINISHED"),
                match(homeId = 81, awayId = 86, homeGoals = 1, awayGoals = 1, status = "FINISHED"),
            ),
        )

        assertEquals(
            Aggregates(
                homeTeam = TeamH2H(id = 81, name = "Barcelona", wins = 2, draws = 1, losses = 0),
                awayTeam = TeamH2H(id = 86, name = "Real Madrid", wins = 0, draws = 1, losses = 2),
                homeWinsPercentage = 67f,
                drawsPercentage = 33f,
                awayWinsPercentage = 0f,
                numberOfMatches = 3,
                totalGoals = 5,
            ),
            dto.toHead2head(id = 2).aggregates,
        )
    }

    @Test
    fun toHead2head_returnsEmptyAggregates_whenTeamIdIsMissing() {
        val dto = Head2headDTO(
            aggregates = AggregatesDTO(
                homeTeam = TeamH2HDTO(id = null, name = "Arsenal", wins = 5, draws = 1, losses = 0),
                awayTeam = TeamH2HDTO(id = 61, name = "Chelsea", wins = 0, draws = 1, losses = 5),
                numberOfMatches = 6,
                totalGoals = 10,
            ),
            matches = listOf(
                match(homeId = 57, awayId = 61, homeGoals = 2, awayGoals = 1, status = "FINISHED"),
            ),
        )

        assertEquals(
            Aggregates(
                homeTeam = TeamH2H(id = 0, name = "Arsenal", wins = 0, draws = 0, losses = 0),
                awayTeam = TeamH2H(id = 61, name = "Chelsea", wins = 0, draws = 0, losses = 0),
                homeWinsPercentage = 0f,
                drawsPercentage = 0f,
                awayWinsPercentage = 0f,
                numberOfMatches = 0,
                totalGoals = 0,
            ),
            dto.toHead2head(id = 3).aggregates,
        )
    }

    // region Factories

    private fun head2headDto(
        homeId: Int,
        homeName: String,
        awayId: Int,
        awayName: String,
        matches: List<MatchDTO>,
    ): Head2headDTO = Head2headDTO(
        aggregates = AggregatesDTO(
            homeTeam = TeamH2HDTO(id = homeId, name = homeName, wins = 99, draws = 99, losses = 99),
            awayTeam = TeamH2HDTO(id = awayId, name = awayName, wins = 99, draws = 99, losses = 99),
            numberOfMatches = 99,
            totalGoals = 99,
        ),
        matches = matches,
    )

    private fun match(
        homeId: Int,
        awayId: Int,
        homeGoals: Int?,
        awayGoals: Int?,
        status: String,
    ): MatchDTO = MatchDTO(
        id = homeId + awayId,
        area = null,
        awayTeam = team(id = awayId),
        competition = null,
        group = null,
        homeTeam = team(id = homeId),
        lastUpdated = null,
        matchDay = null,
        referees = null,
        score = ScoreDTO(
            duration = null,
            winner = null,
            fullTime = TimeDTO(home = homeGoals, away = awayGoals),
            halfTime = null,
        ),
        season = null,
        stage = null,
        status = status,
        utcDate = null,
    )

    private fun team(id: Int): TeamInfoDTO = TeamInfoDTO(
        id = id,
        address = null,
        area = null,
        clubColors = null,
        coach = null,
        crest = null,
        founded = null,
        name = null,
        shortName = null,
        squad = null,
        tla = null,
        venue = null,
        website = null,
    )

    // endregion
}
