package ru.asmelnikov.data.repository

import android.content.ContentUris
import android.content.Intent
import android.provider.CalendarContract
import java.time.Instant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.calendar.MatchCalendarStore
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.repository.MatchCalendarRepository

class MatchCalendarRepositoryImpl internal constructor(
    private val appPackageName: String,
    private val store: MatchCalendarStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MatchCalendarRepository {

    override fun hasCalendarPermission(): Boolean = store.hasPermission()

    override suspend fun findScheduledMatchIds(matchIds: Collection<Int>): Set<Int> {
        return withContext(ioDispatcher) {
            if (!store.hasPermission() || matchIds.isEmpty()) return@withContext emptySet()
            runCatching {
                matchIds.distinct().chunked(QUERY_CHUNK_SIZE)
                    .flatMap { chunk -> store.scheduledMatchIds(appPackageName, chunk) }
                    .toSet()
            }.getOrDefault(emptySet())
        }
    }

    override suspend fun findEventId(matchId: Int): Long? {
        return withContext(ioDispatcher) {
            if (!store.hasPermission()) return@withContext null
            runCatching { store.eventId(appPackageName, matchId) }.getOrNull()
        }
    }

    override fun insertIntent(match: Match): Intent? {
        val startMillis = resolveStartEpochMillis(match)
        if (startMillis <= 0L) return null
        val home = match.homeTeam.shortName.ifBlank { match.homeTeam.name }
        val away = match.awayTeam.shortName.ifBlank { match.awayTeam.name }
        val marker = "$URI_PREFIX${match.id}"
        val description = match.competition.name.ifBlank { marker }.let { body ->
            if (body == marker) marker else "$body\n$marker"
        }
        return Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "$home — $away")
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + MATCH_DURATION_MS)
            putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            putExtra(CalendarContract.Events.HAS_ALARM, 1)
            putExtra(CalendarContract.Reminders.MINUTES, REMINDER_MINUTES_BEFORE)
        }
    }

    override fun viewIntent(match: Match): Intent? {
        val startMillis = resolveStartEpochMillis(match)
        if (startMillis <= 0L) return null
        val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
        ContentUris.appendId(builder, startMillis)
        return Intent(Intent.ACTION_VIEW).setData(builder.build())
    }

    private fun resolveStartEpochMillis(match: Match): Long {
        if (match.startEpochMillis > 0L) return match.startEpochMillis
        return runCatching { Instant.parse(match.utcDate).toEpochMilli() }.getOrDefault(0L)
    }

    private companion object {
        const val URI_PREFIX = "goalpulse://match/"
        const val MATCH_DURATION_MS = 2L * 60 * 60 * 1000
        const val REMINDER_MINUTES_BEFORE = 60
        const val QUERY_CHUNK_SIZE = 100
    }
}
