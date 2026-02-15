@file:Suppress("DEPRECATION")

package ru.asmelnikov.competitions_main

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competitions_main.components.CompetitionItem
import ru.asmelnikov.competitions_main.components.GifImage
import ru.asmelnikov.competitions_main.components.ShimmerListItem
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenSideEffects
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenViewModel
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigateWithArgs
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SharedTransitionScope.CompetitionsScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CompetitionsScreenViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionsScreenSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is CompetitionsScreenSideEffects.OnCompetitionNavigate -> {
                appState.navigateWithArgs(route = Routes.Competition_Standings, args = it.compId)
            }
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )

    CompetitionsScreenContent(
        comps = state.comps,
        updateComps = viewModel::updateCompetitionsFromRemoteToLocal,
        swipeRefreshState = swipeRefreshState,
        isLoading = state.isLoading,
        onCompClick = viewModel::onCompClick,
        animatedVisibilityScope = animatedVisibilityScope
    )

}

@Composable
fun SharedTransitionScope.CompetitionsScreenContent(
    comps: List<Competition>,
    updateComps: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    isLoading: Boolean,
    onCompClick: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {

            val textSize = (18 + (30 - 12) * state.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .pin()
            )

            GifImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .parallax(),
                alpha = state.toolbarState.progress
            )

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.large)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .road(
                        whenCollapsed = Alignment.BottomCenter,
                        whenExpanded = Alignment.BottomCenter
                    )
            )

            Text(
                text = when (state.toolbarState.progress) {
                    0f -> "Available competitions"
                    else -> "Goal pulse!"
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = textSize
                ),
                modifier = Modifier
                    .padding(
                        bottom = MaterialTheme.dimens.small3,
                        top = MaterialTheme.dimens.medium3,
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small3
                    )
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomEnd)
            )

        }) {

        SwipeRefresh(
            swipeEnabled = state.toolbarState.progress == 1f,
            state = swipeRefreshState,
            onRefresh = updateComps
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                when {
                    comps.isEmpty() && isLoading -> {
                        items(10) {
                            ShimmerListItem()
                        }
                    }

                    comps.isEmpty() -> {
                        item {
                            EmptyContent(
                                onReloadClick = updateComps
                            )
                        }
                    }

                    else -> {
                        items(items = comps, key = { it.id }) { comp ->
                            CompetitionItem(
                                competition = comp,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onCompClick = onCompClick
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium4))
                }
            }
        }
    }
}


