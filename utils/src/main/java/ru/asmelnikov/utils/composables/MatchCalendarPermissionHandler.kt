package ru.asmelnikov.utils.composables

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.asmelnikov.utils.calendar.CalendarPermissions
import ru.asmelnikov.utils.calendar.isCalendarPermissionPermanentlyDeclined
import ru.asmelnikov.utils.calendar.openCalendarPermissionSettings

class MatchCalendarPermissionHandler(
    val launchSystemPermission: () -> Unit
)

@Composable
fun rememberMatchCalendarPermissionHandler(
    onPermissionResult: (granted: Boolean) -> Unit
): MatchCalendarPermissionHandler {
    val activity = LocalActivity.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = CalendarPermissions.REQUIRED.all { permission ->
            result[permission] == true
        }
        if (!granted && activity?.isCalendarPermissionPermanentlyDeclined() == true) {
            activity.openCalendarPermissionSettings()
        }
        onPermissionResult(granted)
    }
    return remember(permissionLauncher) {
        MatchCalendarPermissionHandler(
            launchSystemPermission = {
                permissionLauncher.launch(CalendarPermissions.REQUIRED)
            }
        )
    }
}
