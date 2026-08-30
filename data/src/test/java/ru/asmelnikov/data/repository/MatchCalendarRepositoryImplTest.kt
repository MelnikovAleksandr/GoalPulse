package ru.asmelnikov.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.asmelnikov.data.calendar.MatchCalendarStore
import ru.asmelnikov.domain.models.Match

class MatchCalendarRepositoryImplTest {

    private lateinit var store: FakeMatchCalendarStore
    private lateinit var repository: MatchCalendarRepositoryImpl

    @Before
    fun setUp() {
        store = FakeMatchCalendarStore()
        repository = MatchCalendarRepositoryImpl(
            appPackageName = APP_PACKAGE,
            store = store,
            ioDispatcher = Dispatchers.Unconfined,
        )
    }

    @Test
    fun hasCalendarPermission_followsStore() {
        assertTrue(repository.hasCalendarPermission())
        store.permissionGranted = false
        assertFalse(repository.hasCalendarPermission())
    }

    @Test
    fun findScheduledMatchIds_withoutPermission_returnsEmpty() = runBlocking {
        store.permissionGranted = false
        store.storedIds.add(10)

        val result = repository.findScheduledMatchIds(listOf(10, 11))

        assertTrue(result.isEmpty())
    }

    @Test
    fun findScheduledMatchIds_returnsOnlyRequestedOwnEvents() = runBlocking {
        store.storedIds.addAll(listOf(10, 11, 99))

        val result = repository.findScheduledMatchIds(listOf(10, 11, 12))

        assertEquals(setOf(10, 11), result)
    }

    @Test
    fun findEventId_returnsStoredId() = runBlocking {
        store.eventIds[10] = 7L

        assertEquals(7L, repository.findEventId(10))
    }

    @Test
    fun findEventId_whenMissing_returnsNull() = runBlocking {
        assertNull(repository.findEventId(10))
    }

    @Test
    fun findEventId_withoutPermission_returnsNull() = runBlocking {
        store.permissionGranted = false
        store.eventIds[10] = 7L

        assertNull(repository.findEventId(10))
    }

    @Test
    fun findScheduledMatchIds_whenEmpty_doesNotQueryStore() = runBlocking {
        store.storedIds.add(10)

        val result = repository.findScheduledMatchIds(emptyList())

        assertTrue(result.isEmpty())
        assertTrue(store.queriedMatchIds.isEmpty())
    }

    @Test
    fun insertIntent_withoutStartTime_returnsNull() {
        assertNull(repository.insertIntent(Match(id = 10, utcDate = "not-a-date")))
    }

    @Test
    fun viewIntent_withoutStartTime_returnsNull() {
        assertNull(repository.viewIntent(Match(id = 10, utcDate = "not-a-date")))
    }
}

private const val APP_PACKAGE = "ru.asmelnikov.goalpulse"

private class FakeMatchCalendarStore : MatchCalendarStore {
    var permissionGranted: Boolean = true
    val storedIds = mutableListOf<Int>()
    val eventIds = mutableMapOf<Int, Long>()
    val queriedMatchIds = mutableListOf<Collection<Int>>()

    override fun hasPermission(): Boolean = permissionGranted

    override fun eventId(packageName: String, matchId: Int): Long? = eventIds[matchId]

    override fun scheduledMatchIds(packageName: String, matchIds: Collection<Int>): Set<Int> {
        queriedMatchIds += matchIds
        return storedIds.filter { it in matchIds }.toSet()
    }
}
