package ru.asmelnikov.utils.composables

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isPortrait(): Boolean {
    val configuration by rememberUpdatedState(LocalConfiguration.current)
    return configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

@Composable
fun isLandScape(): Boolean {
    val configuration by rememberUpdatedState(LocalConfiguration.current)
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}