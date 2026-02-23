package ru.asmelnikov.competitions_main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ru.asmelnikov.domain.models.getMockCompetitionsList
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigate
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.composables.PullToRefreshWrapper

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
                appState.navigate(route = Routes.Standings(it.compId))
            }
        }
    }

    CompetitionsScreenContent(
        comps = state.comps,
        updateComps = viewModel::updateCompetitionsFromRemoteToLocal,
        isLoading = state.isLoading,
        onCompClick = viewModel::onCompClick,
        animatedVisibilityScope = animatedVisibilityScope
    )

}

@Composable
fun SharedTransitionScope.CompetitionsScreenContent(
    comps: List<Competition>,
    updateComps: () -> Unit,
    isLoading: Boolean,
    onCompClick: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val titleText = when (collapsingState.toolbarState.progress) {
        0f -> stringResource(R.string.available_competitions)
        else -> stringResource(R.string.goal_pulse)
    }
    val textSize = remember(collapsingState.toolbarState.progress) {
        (18 + (30 - 12) * collapsingState.toolbarState.progress).sp
    }

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
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
                alpha = collapsingState.toolbarState.progress
            )

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(dimens.large)
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
                text = titleText,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = textSize
                ),
                modifier = Modifier
                    .statusBarsPadding()
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomEnd)
                    .padding(dimens.small3)
            )
        },
        body = {
            AnimatedContent(targetState = comps.isEmpty()) { isListEmpty ->
                when {
                    isListEmpty && isLoading -> {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(dimens.small3)
                        ) {
                            repeat(10) {
                                ShimmerListItem()
                            }
                        }
                    }

                    isListEmpty -> {
                        EmptyContent(
                            withScroll = true,
                            onReloadClick = updateComps
                        )
                    }

                    else -> {
                        PullToRefreshWrapper(
                            isRefreshing = isLoading,
                            onRefresh = updateComps,
                            enabled = collapsingState.toolbarState.progress == 1f
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(dimens.small3)
                            ) {
                                items(items = comps, key = { it.id }) { comp ->
                                    CompetitionItem(
                                        modifier = Modifier.animateItem(),
                                        competition = comp,
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        onCompClick = onCompClick
                                    )
                                }
                                item {
                                    Spacer(modifier = Modifier.height(dimens.medium4))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, locale = "ru", name = "with data")
@Composable
private fun CompetitionsScreenContentPreview1() {
    GoalPulseTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(visible = true) {
                CompetitionsScreenContent(
                    comps = getMockCompetitionsList(),
                    updateComps = {},
                    isLoading = false,
                    onCompClick = {},
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru", name = "empty data + loading")
@Composable
private fun CompetitionsScreenContentPreview2() {
    GoalPulseTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(visible = true) {
                CompetitionsScreenContent(
                    comps = emptyList(),
                    updateComps = {},
                    isLoading = true,
                    onCompClick = {},
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru", name = "empty data after loading")
@Composable
private fun CompetitionsScreenContentPreview3() {
    GoalPulseTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(visible = true) {
                CompetitionsScreenContent(
                    comps = emptyList(),
                    updateComps = {},
                    isLoading = false,
                    onCompClick = {},
                    animatedVisibilityScope = this
                )
            }
        }
    }
}


