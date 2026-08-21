package ru.asmelnikov.data.calendar

internal interface MatchCalendarStore {
    fun hasPermission(): Boolean
    fun writableCalendarId(): Long?
    fun eventId(packageName: String, customAppUri: String): Long?
    fun scheduledCustomAppUris(packageName: String, customAppUris: Collection<String>): List<String>
    fun insertEvent(
        calendarId: Long,
        title: String,
        description: String,
        startMillis: Long,
        endMillis: Long,
        timeZone: String,
        customAppPackage: String,
        customAppUri: String
    ): Long?

    fun insertAlertReminder(eventId: Long, minutesBefore: Int)
    fun deleteEvent(eventId: Long)
}
