package ru.asmelnikov.goalpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import com.mxalbert.sharedelements.SharedElementsRoot
import ru.asmelnikov.goalpulse.navigation.NavGraph
import ru.asmelnikov.goalpulse.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.composables.rememberAppState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val appState = rememberAppState()

            GoalPulseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = {
                        SnackbarHost(appState.snackbarState)
                    }
                ) {
                    SharedElementsRoot{
                        NavGraph(
                            appState = appState,
                            paddingValues = it,
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