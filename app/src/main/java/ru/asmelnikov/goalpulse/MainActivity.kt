package ru.asmelnikov.goalpulse

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import ru.asmelnikov.goalpulse.navigation.NavGraph
import ru.asmelnikov.utils.composables.rememberAppState
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        window.isNavigationBarContrastEnforced = false
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val appState = rememberAppState()

            GoalPulseTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = {
                        SnackbarHost(
                            hostState = appState.snackbarState,
                            modifier = Modifier.navigationBarsPadding(),
                        )
                    },
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
                                    actionPerformed = action,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
