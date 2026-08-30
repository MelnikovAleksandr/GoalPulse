package ru.asmelnikov.utils.composables

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.asmelnikov.utils.navigation.Routes

class MainAppState(
    val snackbarState: SnackbarHostState,
    val snackbarScope: CoroutineScope,
    val backStack: NavBackStack<NavKey>,
) {

    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        actionPerformed: () -> Unit,
    ) {
        snackbarScope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            val snackResult = snackbarState.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = actionLabel,
            )
            when (snackResult) {
                SnackbarResult.ActionPerformed -> actionPerformed()
                SnackbarResult.Dismissed -> {}
            }
        }
    }
}

@Composable
fun rememberAppState(
    snackbarState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    backStack: NavBackStack<NavKey> = rememberNavBackStack(Routes.Competitions),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
) = remember(snackbarState, backStack, snackbarScope) {
    MainAppState(
        snackbarState = snackbarState,
        snackbarScope = snackbarScope,
        backStack = backStack,
    )
}
