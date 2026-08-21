package ru.asmelnikov.data.calendar

import ru.asmelnikov.domain.models.Match
import java.time.Instant

object MatchCalendarEventMapper {

    private const val URI_PREFIX = "goalpulse://match/"
    const val MATCH_DURATION_MS = 2L * 60 * 60 * 1000
    const val REMINDER_MINUTES_BEFORE = 60

    fun customAppUri(matchId: Int): String = "$URI_PREFIX$matchId"

    fun matchIdFromCustomAppUri(uri: String?): Int? {
        if (uri.isNullOrBlank() || !uri.startsWith(URI_PREFIX)) return null
        return uri.removePrefix(URI_PREFIX).toIntOrNull()
    }

    fun eventTitle(match: Match): String {
        val home = match.homeTeam.shortName.ifBlank { match.homeTeam.name }
        val away = match.awayTeam.shortName.ifBlank { match.awayTeam.name }
        return "$home — $away"
    }

    fun eventDescription(match: Match): String = match.competition.name

    fun resolveStartEpochMillis(match: Match): Long {
        if (match.startEpochMillis > 0L) return match.startEpochMillis
        return runCatching { Instant.parse(match.utcDate).toEpochMilli() }.getOrDefault(0L)
    }

    fun resolveEndEpochMillis(startEpochMillis: Long): Long =
        startEpochMillis + MATCH_DURATION_MS
}
