package ru.asmelnikov.goalpulse.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
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

    NavDisplay(
        modifier = Modifier.padding(paddingValues),
        backStack = appState.backStack,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Routes.Competitions> {
                CompetitionsScreen(
                    appState = appState,
                    showSnackbar = showSnackbar,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                )
            }

            entry<Routes.Standings> {
                CompetitionStandingsScreen(
                    appState = appState,
                    compId = it.compId,
                    showSnackbar = showSnackbar,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                )
            }

            entry<Routes.Team> {
                TeamInfoScreen(
                    appState = appState,
                    teamId = it.teamId,
                    showSnackbar = showSnackbar
                )
            }

            entry<Routes.Person> {
                PersonInfoScreen(
                    appState = appState,
                    personId = it.personId,
                    showSnackbar = showSnackbar
                )
            }
        }
    )
}