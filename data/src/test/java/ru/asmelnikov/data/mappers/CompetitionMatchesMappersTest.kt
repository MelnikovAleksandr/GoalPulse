package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Stage

class CompetitionMatchesMappersTest {

    // region By date (team matches)

    @Test
    fun groupMatchesByDateCompleted_keepsFinishedWithBothTeams_newestFirst() {
        val finishedNew = matchEntity(
            id = 2,
            status = "FINISHED",
            utcDate = "2025-05-10T17:00:00Z",
            homeName = "Arsenal",
            awayName = "Chelsea",
        )
        val finishedOld = matchEntity(
            id = 1,
            status = "FINISHED",
            utcDate = "2025-05-03T15:00:00Z",
            homeName = "Liverpool",
            awayName = "City",
        )
        val timed = matchEntity(
            id = 3,
            status = "TIMED",
            utcDate = "2025-05-17T15:00:00Z",
            homeName = "Arsenal",
            awayName = "City",
        )
        val finishedWithoutAway = matchEntity(
            id = 4,
            status = "FINISHED",
            utcDate = "2025-05-11T15:00:00Z",
            homeName = "Arsenal",
            awayName = "Chelsea",
        ).also { it.awayTeam = null }

        val result = groupMatchesByDateCompleted(
            listOf(finishedOld, timed, finishedNew, finishedWithoutAway),
        )

        assertEquals(
            listOf(
                MatchKey(id = 2, home = "Arsenal", away = "Chelsea"),
                MatchKey(id = 1, home = "Liverpool", away = "City"),
            ),
            result.toMatchKeys(),
        )
    }

    @Test
    fun groupMatchesByDateAhead_keepsUnfinishedWithBothTeams_oldestFirst() {
        val timedSoon = matchEntity(
            id = 10,
            status = "TIMED",
            utcDate = "2025-08-16T15:00:00Z",
            homeName = "Arsenal",
            awayName = "Wolves",
        )
        val liveLater = matchEntity(
            id = 11,
            status = "LIVE",
            utcDate = "2025-08-17T16:30:00Z",
            homeName = "Chelsea",
            awayName = "Liverpool",
        )
        val finished = matchEntity(
            id = 12,
            status = "FINISHED",
            utcDate = "2025-08-10T15:00:00Z",
            homeName = "City",
            awayName = "Arsenal",
        )
        val timedWithoutHome = matchEntity(
            id = 13,
            status = "TIMED",
            utcDate = "2025-08-15T15:00:00Z",
            homeName = "Arsenal",
            awayName = "Everton",
        ).also { it.homeTeam = null }

        val result = groupMatchesByDateAhead(
            listOf(liveLater, finished, timedSoon, timedWithoutHome),
        )

        assertEquals(
            listOf(
                MatchKey(id = 10, home = "Arsenal", away = "Wolves"),
                MatchKey(id = 11, home = "Chelsea", away = "Liverpool"),
            ),
            result.toMatchKeys(),
        )
    }

    // endregion

    // region By tour (competition matches)

    @Test
    fun filterCompletedMatches_forLeague_groupsByTour_matchDaysDescending() {
        val md38late = matchEntity(
            id = 38,
            status = "FINISHED",
            utcDate = "2025-05-25T16:00:00Z",
            matchDay = 38,
            stage = "REGULAR_SEASON",
            homeName = "Arsenal",
            awayName = "City",
        )
        val md38early = matchEntity(
            id = 37,
            status = "FINISHED",
            utcDate = "2025-05-25T13:00:00Z",
            matchDay = 38,
            stage = "REGULAR_SEASON",
            homeName = "Chelsea",
            awayName = "Liverpool",
        )
        val md37 = matchEntity(
            id = 36,
            status = "FINISHED",
            utcDate = "2025-05-18T15:00:00Z",
            matchDay = 37,
            stage = "REGULAR_SEASON",
            homeName = "Tottenham",
            awayName = "United",
        )
        val timed = matchEntity(
            id = 39,
            status = "TIMED",
            utcDate = "2025-08-16T15:00:00Z",
            matchDay = 1,
            stage = "REGULAR_SEASON",
            homeName = "Arsenal",
            awayName = "Wolves",
        )

        val result = filterCompletedMatches(
            matchesEntity(seasonType = "LEAGUE", matches = listOf(md37, timed, md38early, md38late)),
        )

        assertEquals(
            listOf(
                TourKey(matchDay = 38, stage = Stage.REGULAR_SEASON, matchIds = listOf(38, 37)),
                TourKey(matchDay = 37, stage = Stage.REGULAR_SEASON, matchIds = listOf(36)),
            ),
            result.toTourKeys(),
        )
    }

    @Test
    fun filterAheadMatches_forLeague_groupsByTour_matchDaysAscending() {
        val md1 = matchEntity(
            id = 1,
            status = "TIMED",
            utcDate = "2025-08-16T15:00:00Z",
            matchDay = 1,
            stage = "REGULAR_SEASON",
            homeName = "Arsenal",
            awayName = "Wolves",
        )
        val md2early = matchEntity(
            id = 2,
            status = "SCHEDULED",
            utcDate = "2025-08-23T12:30:00Z",
            matchDay = 2,
            stage = "REGULAR_SEASON",
            homeName = "Chelsea",
            awayName = "West Ham",
        )
        val md2late = matchEntity(
            id = 3,
            status = "TIMED",
            utcDate = "2025-08-23T17:00:00Z",
            matchDay = 2,
            stage = "REGULAR_SEASON",
            homeName = "Liverpool",
            awayName = "City",
        )
        val finished = matchEntity(
            id = 4,
            status = "FINISHED",
            utcDate = "2025-05-25T15:00:00Z",
            matchDay = 38,
            stage = "REGULAR_SEASON",
            homeName = "Arsenal",
            awayName = "City",
        )

        val result = filterAheadMatches(
            matchesEntity(seasonType = "LEAGUE", matches = listOf(md2late, finished, md1, md2early)),
        )

        assertEquals(
            listOf(
                TourKey(matchDay = 1, stage = Stage.REGULAR_SEASON, matchIds = listOf(1)),
                TourKey(matchDay = 2, stage = Stage.REGULAR_SEASON, matchIds = listOf(2, 3)),
            ),
            result.toTourKeys(),
        )
    }

    @Test
    fun filterCompletedMatches_forCup_sortsToursByDateDescending_notByMatchDay() {
        val quarterFinal = matchEntity(
            id = 20,
            status = "FINISHED",
            utcDate = "2025-04-08T19:00:00Z",
            matchDay = 1,
            stage = "QUARTER_FINALS",
            homeName = "Arsenal",
            awayName = "Real Madrid",
        )
        val semiFinal = matchEntity(
            id = 21,
            status = "FINISHED",
            utcDate = "2025-04-29T19:00:00Z",
            matchDay = 1,
            stage = "SEMI_FINALS",
            homeName = "Arsenal",
            awayName = "PSG",
        )

        val result = filterCompletedMatches(
            matchesEntity(seasonType = "CUP", matches = listOf(quarterFinal, semiFinal)),
        )

        assertEquals(
            listOf(
                TourKey(matchDay = 1, stage = Stage.SEMI_FINALS, matchIds = listOf(21)),
                TourKey(matchDay = 1, stage = Stage.QUARTER_FINALS, matchIds = listOf(20)),
            ),
            result.toTourKeys(),
        )
    }

    @Test
    fun filterAheadMatches_forCup_sortsToursByDateAscending() {
        val last16 = matchEntity(
            id = 30,
            status = "TIMED",
            utcDate = "2025-03-04T20:00:00Z",
            matchDay = 1,
            stage = "LAST_16",
            homeName = "Barcelona",
            awayName = "Benfica",
        )
        val quarterFinal = matchEntity(
            id = 31,
            status = "SCHEDULED",
            utcDate = "2025-04-08T19:00:00Z",
            matchDay = 1,
            stage = "QUARTER_FINALS",
            homeName = "Barcelona",
            awayName = "Bayern",
        )

        val result = filterAheadMatches(
            matchesEntity(seasonType = "CUP", matches = listOf(quarterFinal, last16)),
        )

        assertEquals(
            listOf(
                TourKey(matchDay = 1, stage = Stage.LAST_16, matchIds = listOf(30)),
                TourKey(matchDay = 1, stage = Stage.QUARTER_FINALS, matchIds = listOf(31)),
            ),
            result.toTourKeys(),
        )
    }

    // endregion

    // region Factories

    private fun matchesEntity(
        seasonType: String,
        matches: List<MatchEntity>,
    ): MatchesEntity {
        val entity = MatchesEntity()
        entity.id = seasonType
        entity.seasonType = seasonType
        entity.matches = realmListOf<MatchEntity>().apply { addAll(matches) }
        return entity
    }

    private fun matchEntity(
        id: Int,
        status: String,
        utcDate: String,
        homeName: String,
        awayName: String,
        matchDay: Int = -1,
        stage: String = "",
    ): MatchEntity {
        val entity = MatchEntity()
        entity.id = id
        entity.status = status
        entity.utcDate = utcDate
        entity.matchDay = matchDay
        entity.stage = stage
        entity.homeTeam = team(id = id * 10, name = homeName)
        entity.awayTeam = team(id = id * 10 + 1, name = awayName)
        return entity
    }

    private fun team(id: Int, name: String): TeamEmbeddedEntity {
        val entity = TeamEmbeddedEntity()
        entity.id = id
        entity.name = name
        return entity
    }

    // endregion

    // region Keys

    private fun List<Match>.toMatchKeys(): List<MatchKey> = map {
        MatchKey(id = it.id, home = it.homeTeam.name, away = it.awayTeam.name)
    }

    private fun List<MatchesByTour>.toTourKeys(): List<TourKey> = map { tour ->
        TourKey(
            matchDay = tour.matchDay,
            stage = tour.stage,
            matchIds = tour.matches.map { it.id },
        )
    }

    private data class MatchKey(val id: Int, val home: String, val away: String)

    private data class TourKey(val matchDay: Int, val stage: Stage, val matchIds: List<Int>)

    // endregion
}
