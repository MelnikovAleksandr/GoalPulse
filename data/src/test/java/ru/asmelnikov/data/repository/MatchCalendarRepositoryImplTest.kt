package ru.asmelnikov.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.asmelnikov.data.calendar.MatchCalendarEventMapper
import ru.asmelnikov.data.calendar.MatchCalendarStore
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchTeam
import ru.asmelnikov.utils.Resource

class MatchCalendarRepositoryImplTest {

    private lateinit var store: FakeMatchCalendarStore
    private lateinit var repository: MatchCalendarRepositoryImpl

    @Before
    fun setUp() {
        store = FakeMatchCalendarStore()
        repository = MatchCalendarRepositoryImpl(
            appPackageName = APP_PACKAGE,
            store = store,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun findScheduledMatchIds_withoutPermission_returnsEmpty() = runBlocking {
        store.permissionGranted = false
        store.storedUris.add("goalpulse://match/10")

        val result = repository.findScheduledMatchIds(listOf(10, 11))

        assertTrue(result.isEmpty())
    }

    @Test
    fun findScheduledMatchIds_returnsOnlyRequestedOwnEvents() = runBlocking {
        store.storedUris.addAll(
            listOf(
                "goalpulse://match/10",
                "goalpulse://match/11",
                "goalpulse://match/99"
            )
        )

        val result = repository.findScheduledMatchIds(listOf(10, 11, 12))

        assertEquals(setOf(10, 11), result)
    }

    @Test
    fun addMatch_withoutPermission_returnsError() = runBlocking {
        store.permissionGranted = false

        val result = repository.addMatch(aheadMatch)

        assertTrue(result is Resource.Error)
        assertTrue(store.insertedTitles.isEmpty())
    }

    @Test
    fun addMatch_withoutStartTime_returnsError() = runBlocking {
        val result = repository.addMatch(Match(id = 10, utcDate = "not-a-date"))

        assertTrue(result is Resource.Error)
        assertTrue(store.insertedTitles.isEmpty())
    }

    @Test
    fun addMatch_withoutWritableCalendar_returnsError() = runBlocking {
        store.writableCalendarId = null

        val result = repository.addMatch(aheadMatch)

        assertTrue(result is Resource.Error)
        assertTrue(store.insertedTitles.isEmpty())
    }

    @Test
    fun addMatch_insertsEventAndHourReminder() = runBlocking {
        val result = repository.addMatch(aheadMatch)

        assertTrue(result is Resource.Success)
        assertEquals(listOf("Arsenal — Chelsea"), store.insertedTitles)
        assertEquals(listOf("Premier League"), store.insertedDescriptions)
        assertEquals(listOf(START_MILLIS), store.insertedStarts)
        assertEquals(
            listOf(START_MILLIS + MatchCalendarEventMapper.MATCH_DURATION_MS),
            store.insertedEnds
        )
        assertEquals(listOf("goalpulse://match/10"), store.insertedUris)
        assertEquals(
            listOf(1L to MatchCalendarEventMapper.REMINDER_MINUTES_BEFORE),
            store.reminders
        )
    }

    @Test
    fun addMatch_whenAlreadyScheduled_doesNotInsertAgain() = runBlocking {
        store.eventIds["goalpulse://match/10"] = 5L

        val result = repository.addMatch(aheadMatch)

        assertTrue(result is Resource.Success)
        assertTrue(store.insertedTitles.isEmpty())
        assertTrue(store.reminders.isEmpty())
    }

    @Test
    fun removeMatch_deletesExistingEvent() = runBlocking {
        store.eventIds["goalpulse://match/10"] = 7L

        val result = repository.removeMatch(10)

        assertTrue(result is Resource.Success)
        assertEquals(listOf(7L), store.deletedEventIds)
    }

    @Test
    fun removeMatch_whenMissing_isSuccess() = runBlocking {
        val result = repository.removeMatch(10)

        assertTrue(result is Resource.Success)
        assertTrue(store.deletedEventIds.isEmpty())
    }

    @Test
    fun removeMatch_withoutPermission_returnsError() = runBlocking {
        store.permissionGranted = false
        store.eventIds["goalpulse://match/10"] = 7L

        val result = repository.removeMatch(10)

        assertTrue(result is Resource.Error)
        assertTrue(store.deletedEventIds.isEmpty())
    }
}

private const val APP_PACKAGE = "ru.asmelnikov.goalpulse"
private const val START_MILLIS = 1_746_792_000_000L

private val aheadMatch = Match(
    id = 10,
    utcDate = "2026-05-09T14:00:00Z",
    startEpochMillis = START_MILLIS,
    competition = Competition(name = "Premier League"),
    homeTeam = MatchTeam(name = "Arsenal Football Club", shortName = "Arsenal"),
    awayTeam = MatchTeam(name = "Chelsea Football Club", shortName = "Chelsea")
)

private class FakeMatchCalendarStore : MatchCalendarStore {
    var permissionGranted: Boolean = true
    var writableCalendarId: Long? = 1L
    val storedUris = mutableListOf<String>()
    val eventIds = mutableMapOf<String, Long>()
    val insertedTitles = mutableListOf<String>()
    val insertedDescriptions = mutableListOf<String>()
    val insertedStarts = mutableListOf<Long>()
    val insertedEnds = mutableListOf<Long>()
    val insertedUris = mutableListOf<String>()
    val reminders = mutableListOf<Pair<Long, Int>>()
    val deletedEventIds = mutableListOf<Long>()
    private var nextEventId = 1L

    override fun hasPermission(): Boolean = permissionGranted

    override fun writableCalendarId(): Long? = writableCalendarId

    override fun eventId(packageName: String, customAppUri: String): Long? {
        return eventIds[customAppUri]
    }

    override fun scheduledCustomAppUris(
        packageName: String,
        customAppUris: Collection<String>
    ): List<String> {
        return storedUris.filter { it in customAppUris }
    }

    override fun insertEvent(
        calendarId: Long,
        title: String,
        description: String,
        startMillis: Long,
        endMillis: Long,
        timeZone: String,
        customAppPackage: String,
        customAppUri: String
    ): Long {
        insertedTitles += title
        insertedDescriptions += description
        insertedStarts += startMillis
        insertedEnds += endMillis
        insertedUris += customAppUri
        val eventId = nextEventId++
        eventIds[customAppUri] = eventId
        storedUris += customAppUri
        return eventId
    }

    override fun insertAlertReminder(eventId: Long, minutesBefore: Int) {
        reminders += eventId to minutesBefore
    }

    override fun deleteEvent(eventId: Long) {
        deletedEventIds += eventId
    }
}
