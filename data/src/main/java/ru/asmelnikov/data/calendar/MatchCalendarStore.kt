package ru.asmelnikov.data.calendar

internal interface MatchCalendarStore {
    fun hasPermission(): Boolean
    fun eventId(packageName: String, matchId: Int): Long?
    fun scheduledMatchIds(packageName: String, matchIds: Collection<Int>): Set<Int>
}
