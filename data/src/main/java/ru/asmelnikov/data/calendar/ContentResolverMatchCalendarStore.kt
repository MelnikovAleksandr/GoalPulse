package ru.asmelnikov.data.calendar

import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import ru.asmelnikov.utils.calendar.CalendarPermissions

internal class ContentResolverMatchCalendarStore(private val context: Context) : MatchCalendarStore {

    private val contentResolver get() = context.contentResolver

    override fun hasPermission(): Boolean = CalendarPermissions.REQUIRED.all { permission ->
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun eventId(packageName: String, matchId: Int): Long? {
        val customAppUri = customAppUri(matchId)
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.CUSTOM_APP_URI,
                CalendarContract.Events.DESCRIPTION,
            ),
            "${CalendarContract.Events.DELETED} = 0 AND (" +
                "(${CalendarContract.Events.CUSTOM_APP_PACKAGE} = ? AND " +
                "${CalendarContract.Events.CUSTOM_APP_URI} = ?) OR " +
                "${CalendarContract.Events.DESCRIPTION} LIKE ?)",
            arrayOf(packageName, customAppUri, "%$customAppUri%"),
            null,
        ) ?: return null
        return cursor.use {
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            val uriIndex = it.getColumnIndex(CalendarContract.Events.CUSTOM_APP_URI)
            val descriptionIndex = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
            if (idIndex < 0) return@use null
            while (it.moveToNext()) {
                val eventId = it.getLong(idIndex)
                val storedUri = if (uriIndex >= 0) it.getString(uriIndex) else null
                if (storedUri == customAppUri) return@use eventId
                val description = if (descriptionIndex >= 0) it.getString(descriptionIndex) else null
                if (matchIdsFromDescription(description).contains(matchId)) {
                    return@use eventId
                }
            }
            null
        }
    }

    override fun scheduledMatchIds(packageName: String, matchIds: Collection<Int>): Set<Int> {
        if (matchIds.isEmpty()) return emptySet()
        val requested = matchIds.toSet()
        return buildSet {
            addAll(queryByCustomAppUri(packageName, requested))
            addAll(queryByDescription(requested))
        }
    }

    private fun queryByCustomAppUri(packageName: String, matchIds: Set<Int>): Set<Int> {
        val uris = matchIds.map(::customAppUri)
        val placeholders = uris.joinToString(separator = ",") { "?" }
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
        val selectionArgs = arrayOf(packageName) + uris
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events.CUSTOM_APP_URI),
            selection,
            selectionArgs,
            null,
        ) ?: return emptySet()
        return cursor.use {
            val uriIndex = it.getColumnIndex(CalendarContract.Events.CUSTOM_APP_URI)
            if (uriIndex < 0) {
                emptySet()
            } else {
                buildSet {
                    while (it.moveToNext()) {
                        matchIdFromCustomAppUri(it.getString(uriIndex))
                            ?.takeIf(matchIds::contains)
                            ?.let(::add)
                    }
                }
            }
        }
    }

    private fun queryByDescription(matchIds: Set<Int>): Set<Int> {
        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events.DESCRIPTION),
            "${CalendarContract.Events.DELETED} = 0 AND " +
                "${CalendarContract.Events.DESCRIPTION} LIKE ?",
            arrayOf("%$URI_PREFIX%"),
            null,
        ) ?: return emptySet()
        return cursor.use {
            val descriptionIndex = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
            if (descriptionIndex < 0) {
                emptySet()
            } else {
                buildSet {
                    while (it.moveToNext()) {
                        matchIdsFromDescription(it.getString(descriptionIndex))
                            .filter(matchIds::contains)
                            .forEach(::add)
                    }
                }
            }
        }
    }

    private companion object {
        const val URI_PREFIX = "goalpulse://match/"
        private val markerRegex = Regex("""goalpulse://match/(\d+)""")

        fun customAppUri(matchId: Int): String = "$URI_PREFIX$matchId"

        fun matchIdFromCustomAppUri(uri: String?): Int? {
            if (uri.isNullOrBlank() || !uri.startsWith(URI_PREFIX)) return null
            return uri.removePrefix(URI_PREFIX).toIntOrNull()
        }

        fun matchIdsFromDescription(description: String?): Set<Int> {
            if (description.isNullOrBlank()) return emptySet()
            return markerRegex.findAll(description)
                .mapNotNull { it.groupValues[1].toIntOrNull() }
                .toSet()
        }
    }
}
