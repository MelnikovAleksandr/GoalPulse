package ru.asmelnikov.utils.composables

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainAppState(
    val snackbarState: SnackbarHostState,
    val snackbarScope: CoroutineScope,
    val navController: NavHostController
) {

    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        actionPerformed: () -> Unit
    ) {

        snackbarScope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            val snackResult = snackbarState.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = actionLabel
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
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarState, navController, snackbarScope) {
    MainAppState(
        snackbarState = snackbarState,
        snackbarScope = snackbarScope,
        navController = navController
    )
}