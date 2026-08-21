package ru.asmelnikov.data.calendar

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import ru.asmelnikov.utils.calendar.CalendarPermissions

internal class ContentResolverMatchCalendarStore(
    private val context: Context
) : MatchCalendarStore {

    private val contentResolver get() = context.contentResolver

    override fun hasPermission(): Boolean {
        return CalendarPermissions.REQUIRED.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun writableCalendarId(): Long? {
        val cursor = contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.IS_PRIMARY
            ),
            "${CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL} >= ? AND " +
                "${CalendarContract.Calendars.VISIBLE} = 1",
            arrayOf(CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR.toString()),
            null
        ) ?: return null
        return cursor.use {
            val idIndex = it.getColumnIndex(CalendarContract.Calendars._ID)
            val primaryIndex = it.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)
            if (idIndex < 0) return@use null
            var fallbackId: Long? = null
            while (it.moveToNext()) {
                val calendarId = it.getLong(idIndex)
                val isPrimary = primaryIndex >= 0 && it.getInt(primaryIndex) == 1
                if (isPrimary) return@use calendarId
                if (fallbackId == null) fallbackId = calendarId
            }
            fallbackId
        }
    }

    override fun eventId(packageName: String, customAppUri: String): Long? {
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events._ID),
            "${CalendarContract.Events.CUSTOM_APP_PACKAGE} = ? AND " +
                "${CalendarContract.Events.CUSTOM_APP_URI} = ? AND " +
                "${CalendarContract.Events.DELETED} = 0",
            arrayOf(packageName, customAppUri),
            null
        ) ?: return null
        return cursor.use {
            if (!it.moveToFirst()) return@use null
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            if (idIndex < 0) null else it.getLong(idIndex)
        }
    }

    override fun scheduledCustomAppUris(
        packageName: String,
        customAppUris: Collection<String>
    ): List<String> {
        if (customAppUris.isEmpty()) return emptyList()
        val placeholders = customAppUris.joinToString(separator = ",") { "?" }
        val selection = buildString {
            append(CalendarContract.Events.CUSTOM_APP_PACKAGE)
            append(" = ? AND ")
            append(CalendarContract.Events.DELETED)
            append(" = 0 AND ")
            append(CalendarContract.Events.CUSTOM_APP_URI)
            append(" IN (")
            append(placeholders)
            append(")")
        }
        val selectionArgs = arrayOf(packageName) + customAppUris.toList()
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events.CUSTOM_APP_URI),
            selection,
            selectionArgs,
            null
        ) ?: return emptyList()
        return cursor.use {
            val uriIndex = it.getColumnIndex(CalendarContract.Events.CUSTOM_APP_URI)
            if (uriIndex < 0) {
                emptyList()
            } else {
                buildList {
                    while (it.moveToNext()) {
                        add(it.getString(uriIndex))
                    }
                }
            }
        }
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
    ): Long? {
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone)
            put(CalendarContract.Events.CUSTOM_APP_PACKAGE, customAppPackage)
            put(CalendarContract.Events.CUSTOM_APP_URI, customAppUri)
            put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            put(CalendarContract.Events.HAS_ALARM, 1)
        }
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values) ?: return null
        return ContentUris.parseId(uri)
    }

    override fun insertAlertReminder(eventId: Long, minutesBefore: Int) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId)
            put(CalendarContract.Reminders.MINUTES, minutesBefore)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
    }

    override fun deleteEvent(eventId: Long) {
        val deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        contentResolver.delete(deleteUri, null, null)
    }
}
