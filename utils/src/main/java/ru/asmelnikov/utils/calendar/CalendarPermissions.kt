package ru.asmelnikov.utils.calendar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object CalendarPermissions {
    val REQUIRED = arrayOf(
        Manifest.permission.READ_CALENDAR
    )
}

fun Activity.openCalendarPermissionSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
    )
}

fun Activity.isCalendarPermissionPermanentlyDeclined(): Boolean {
    return CalendarPermissions.REQUIRED.any { permission ->
        !shouldShowRequestPermissionRationale(permission)
    }
}
