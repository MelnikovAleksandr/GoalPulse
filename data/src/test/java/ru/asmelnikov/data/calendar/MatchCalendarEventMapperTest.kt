package ru.asmelnikov.data.calendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchTeam

class MatchCalendarEventMapperTest {

    @Test
    fun customAppUri_usesStableMatchId() {
        assertEquals("goalpulse://match/538046", MatchCalendarEventMapper.customAppUri(538046))
    }

    @Test
    fun matchIdFromCustomAppUri_readsOwnEventsOnly() {
        assertEquals(538046, MatchCalendarEventMapper.matchIdFromCustomAppUri("goalpulse://match/538046"))
        assertNull(MatchCalendarEventMapper.matchIdFromCustomAppUri("https://example.com/538046"))
        assertNull(MatchCalendarEventMapper.matchIdFromCustomAppUri(null))
    }

    @Test
    fun eventTitle_prefersShortNames() {
        val match = Match(
            homeTeam = MatchTeam(name = "Arsenal Football Club", shortName = "Arsenal"),
            awayTeam = MatchTeam(name = "Chelsea Football Club", shortName = "Chelsea")
        )

        assertEquals("Arsenal — Chelsea", MatchCalendarEventMapper.eventTitle(match))
    }

    @Test
    fun resolveStartEpochMillis_prefersExplicitMillis() {
        val match = Match(
            utcDate = "2026-05-09T14:00:00Z",
            startEpochMillis = 1_746_792_000_000L
        )

        assertEquals(1_746_792_000_000L, MatchCalendarEventMapper.resolveStartEpochMillis(match))
    }

    @Test
    fun resolveStartEpochMillis_parsesIsoWhenMillisMissing() {
        val match = Match(utcDate = "2026-05-09T14:00:00Z")

        assertEquals(1_778_335_200_000L, MatchCalendarEventMapper.resolveStartEpochMillis(match))
    }

    @Test
    fun resolveEndEpochMillis_isTwoHoursAfterStart() {
        assertEquals(
            7_200_000L,
            MatchCalendarEventMapper.resolveEndEpochMillis(0L)
        )
    }

    @Test
    fun eventDescription_usesCompetitionName() {
        val match = Match(competition = Competition(name = "Premier League"))

        assertEquals("Premier League", MatchCalendarEventMapper.eventDescription(match))
    }

    @Test
    fun reminderIsOneHourBeforeKickoff() {
        assertEquals(60, MatchCalendarEventMapper.REMINDER_MINUTES_BEFORE)
    }
}
