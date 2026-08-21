package ru.asmelnikov.utils.composables

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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

@Composable
fun SyncMatchCalendarOnReturn(onReturn: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        var leftTheScreen = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> leftTheScreen = true
                Lifecycle.Event.ON_RESUME -> if (leftTheScreen) onReturn()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
