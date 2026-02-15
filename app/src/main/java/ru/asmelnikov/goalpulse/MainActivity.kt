package ru.asmelnikov.goalpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import ru.asmelnikov.goalpulse.navigation.NavGraph
import ru.asmelnikov.goalpulse.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.composables.rememberAppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val appState = rememberAppState()

            GoalPulseTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = {
                        SnackbarHost(appState.snackbarState)
                    }
                ) { paddingValues ->
                    SharedTransitionLayout {
                        NavGraph(
                            appState = appState,
                            paddingValues = paddingValues,
                            showSnackbar = { message, duration, label, action ->
                                appState.showSnackbar(
                                    message = message,
                                    duration = duration,
                                    actionLabel = label,
                                    actionPerformed = action
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}