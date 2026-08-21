package ru.asmelnikov.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.calendar.MatchCalendarEventMapper
import ru.asmelnikov.data.calendar.MatchCalendarStore
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.repository.MatchCalendarRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.util.TimeZone

class MatchCalendarRepositoryImpl internal constructor(
    private val appPackageName: String,
    private val store: MatchCalendarStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MatchCalendarRepository {

    override fun hasCalendarPermission(): Boolean = store.hasPermission()

    override suspend fun findScheduledMatchIds(matchIds: Collection<Int>): Set<Int> {
        return withContext(ioDispatcher) {
            if (!store.hasPermission() || matchIds.isEmpty()) return@withContext emptySet()
            runCatching {
                matchIds.asSequence().distinct()
                    .chunked(QUERY_CHUNK_SIZE)
                    .flatMap { chunk ->
                        store.scheduledCustomAppUris(
                            appPackageName,
                            chunk.map(MatchCalendarEventMapper::customAppUri)
                        )
                    }
                    .mapNotNull(MatchCalendarEventMapper::matchIdFromCustomAppUri)
                    .toSet()
            }.getOrDefault(emptySet())
        }
    }

    override suspend fun addMatch(match: Match): Resource<Unit> {
        return withContext(ioDispatcher) {
            if (!store.hasPermission()) {
                return@withContext permissionError()
            }
            val startMillis = MatchCalendarEventMapper.resolveStartEpochMillis(match)
            if (startMillis <= 0L) {
                return@withContext Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
            }
            val calendarId = store.writableCalendarId()
                ?: return@withContext Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
            val customAppUri = MatchCalendarEventMapper.customAppUri(match.id)
            if (store.eventId(appPackageName, customAppUri) != null) {
                return@withContext Resource.Success(Unit)
            }
            runCatching {
                val eventId = store.insertEvent(
                    calendarId = calendarId,
                    title = MatchCalendarEventMapper.eventTitle(match),
                    description = MatchCalendarEventMapper.eventDescription(match),
                    startMillis = startMillis,
                    endMillis = MatchCalendarEventMapper.resolveEndEpochMillis(startMillis),
                    timeZone = TimeZone.getDefault().id,
                    customAppPackage = appPackageName,
                    customAppUri = customAppUri
                ) ?: return@withContext Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
                runCatching {
                    store.insertAlertReminder(
                        eventId,
                        MatchCalendarEventMapper.REMINDER_MINUTES_BEFORE
                    )
                }
                Resource.Success(Unit)
            }.getOrElse {
                Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
            }
        }
    }

    override suspend fun removeMatch(matchId: Int): Resource<Unit> {
        return withContext(ioDispatcher) {
            if (!store.hasPermission()) {
                return@withContext permissionError()
            }
            runCatching {
                val eventId = store.eventId(
                    appPackageName,
                    MatchCalendarEventMapper.customAppUri(matchId)
                ) ?: return@withContext Resource.Success(Unit)
                store.deleteEvent(eventId)
                Resource.Success(Unit)
            }.getOrElse {
                Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
            }
        }
    }

    private fun permissionError(): Resource<Unit> {
        return Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError())
    }

    private companion object {
        const val QUERY_CHUNK_SIZE = 100
    }
}
