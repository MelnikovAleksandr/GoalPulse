package ru.asmelnikov.goalpulse.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.asmelnikov.competition_standings.CompetitionStandingsScreen
import ru.asmelnikov.competitions_main.CompetitionsScreen
import ru.asmelnikov.person_info.PersonInfoScreen
import ru.asmelnikov.team_info.TeamInfoScreen
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.Routes

@Composable
fun SharedTransitionScope.NavGraph(
    appState: MainAppState,
    paddingValues: PaddingValues,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit
) {

    NavHost(
        navController = appState.navController,
        startDestination = Routes.Competitions_Main,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = Routes.Competitions_Main) {
            CompetitionsScreen(
                appState = appState,
                showSnackbar = showSnackbar,
                animatedVisibilityScope = this
            )
        }
        composable(
            route = "${Routes.Competition_Standings}/{compId}",
            arguments = listOf(
                navArgument("compId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    "${Routes.Team_Info}/{teamId}" ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "${Routes.Team_Info}/{teamId}" ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            }) {
            CompetitionStandingsScreen(
                appState = appState,
                showSnackbar = showSnackbar,
                animatedVisibilityScope = this
            )
        }
        composable(
            route = "${Routes.Team_Info}/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    "${Routes.Competition_Standings}/{compId}" ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "${Routes.Competition_Standings}/{compId}" ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            }) {
            TeamInfoScreen(
                appState = appState,
                showSnackbar = showSnackbar
            )
        }

        composable(
            route = "${Routes.Person_Info}/{personId}",
            arguments = listOf(
                navArgument("personId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    "${Routes.Competition_Standings}/{compId}", "${Routes.Team_Info}/{teamId}" ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "${Routes.Competition_Standings}/{compId}", "${Routes.Team_Info}/{teamId}" ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )

                    else -> null
                }
            }) {
            PersonInfoScreen(
                appState = appState,
                showSnackbar = showSnackbar
            )
        }
    }
}