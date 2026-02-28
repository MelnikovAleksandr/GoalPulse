package ru.asmelnikov.team_info

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.getMockMatchesAhead
import ru.asmelnikov.domain.models.getMockMatchesComplete
import ru.asmelnikov.domain.models.getMockTeam
import ru.asmelnikov.team_info.components.SquadPagerList
import ru.asmelnikov.team_info.components.TabsTeam
import ru.asmelnikov.team_info.components.TeamInfoPage
import ru.asmelnikov.team_info.components.TeamMatchesList
import ru.asmelnikov.team_info.components.Toolbar
import ru.asmelnikov.team_info.view_model.TeamInfoSideEffects
import ru.asmelnikov.team_info.view_model.TeamInfoViewModel
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.composables.isPortrait
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigate
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TeamInfoScreen(
    appState: MainAppState,
    teamId: String,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: TeamInfoViewModel = koinViewModel(parameters = { parametersOf(teamId) })
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is TeamInfoSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is TeamInfoSideEffects.BackClick -> appState.popUp()

            is TeamInfoSideEffects.OnPersonInfoNavigate -> {
                appState.navigate(route = Routes.Person(it.personId))
            }

        }
    }

    TeamInfoScreenContent(
        teamInfo = state.teamInfo,
        isLoading = state.isInfoLoading,
        onBackClick = viewModel::onBackClick,
        colors = state.colorPalette,
        onTeamInfoReload = viewModel::getTeamInfoFromRemoteToLocal,
        isMatchesLoading = state.isMatchesLoading,
        expandedItemId = state.expandedItem,
        onMatchItemClick = viewModel::matchItemClick,
        head2head = state.head2head,
        isHead2headLoading = state.isHead2headLoading,
        teamId = state.teamId,
        matchesComplete = state.matchesComplete,
        matchesAhead = state.matchesAhead,
        onMatchesReload = viewModel::getTeamMatchesFromRemoteToLocal,
        onPersonClick = viewModel::onPersonClick,
        news = state.news,
        isLoadingNews = state.isNewsLoading
    )

}

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun TeamInfoScreenContent(
    teamInfo: TeamInfo,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    colors: Map<String, String>,
    onTeamInfoReload: () -> Unit,
    isMatchesLoading: Boolean,
    matchesComplete: List<Match>,
    matchesAhead: List<Match>,
    teamId: String,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean,
    onMatchesReload: () -> Unit,
    onPersonClick: (Int) -> Unit,
    news: News,
    isLoadingNews: Boolean
) {

    val isMaterialColors = remember(teamInfo.crest) { teamInfo.crest.endsWith(".svg") }
    var vibrant by remember { mutableStateOf("#ffffff") }
    var lightMutedSwatch by remember { mutableStateOf("#ffffff") }
    var onDarkVibrant by remember { mutableStateOf("#ffffff") }
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { TabsTeam.entries.count() }
    )
    LaunchedEffect(key1 = configuration.orientation) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            collapsingState.toolbarState.collapse()
        }
    }
    LaunchedEffect(key1 = colors) {
        if (colors.isNotEmpty()) {
            vibrant = colors["vibrant"] ?: ""
            lightMutedSwatch = colors["lightMuted"] ?: ""
            onDarkVibrant = colors["onDarkVibrant"] ?: ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = collapsingState,
            enabled = isPortrait(),
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                Toolbar(
                    collapsingState = collapsingState,
                    teamName = teamInfo.name,
                    teamCrest = teamInfo.crest,
                    lightMutedSwatch = lightMutedSwatch,
                    onDarkVibrant = onDarkVibrant,
                    isMaterialColors = isMaterialColors
                )
            },
            body = {
                Column(modifier = Modifier.fillMaxSize()) {

                    AnimatedVisibility(visible = isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.extraSmall1),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else Color(
                                lightMutedSwatch.toColorInt()
                            )
                        )
                    }

                    PagerTabRow(
                        tabTitles = TabsTeam.entries.map { stringResource(it.stringResId) },
                        selectedIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                        containerColor = if (isMaterialColors) MaterialTheme.colorScheme.background else Color(
                            vibrant.toColorInt()
                        )
                    )

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = pagerState,
                        beyondViewportPageCount = 1,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        when (page) {
                            0 -> {
                                SquadPagerList(
                                    teamInfo = teamInfo,
                                    stickyHeaderColor = Color(lightMutedSwatch.toColorInt()),
                                    itemColor = Color(vibrant.toColorInt()),
                                    isMaterialColors = isMaterialColors,
                                    isLoading = isLoading,
                                    onReloadClick = onTeamInfoReload,
                                    onPersonClick = onPersonClick
                                )
                            }

                            1 -> {
                                TeamInfoPage(
                                    teamInfo = teamInfo,
                                    isLoading = isLoading,
                                    onReloadClick = onTeamInfoReload,
                                    stickyHeaderColor = Color(lightMutedSwatch.toColorInt()),
                                    itemColor = Color(vibrant.toColorInt()),
                                    isMaterialColors = isMaterialColors,
                                    news = news,
                                    isLoadingNews = isLoadingNews
                                )
                            }

                            2 -> {
                                TeamMatchesList(
                                    matchesCompleted = matchesComplete,
                                    matchesAhead = matchesAhead,
                                    isLoading = isMatchesLoading,
                                    onReloadClick = onMatchesReload,
                                    isMaterialColors = isMaterialColors,
                                    stickyHeaderColor = Color(lightMutedSwatch.toColorInt()),
                                    itemColor = Color(vibrant.toColorInt()),
                                    expandedItemId = expandedItemId,
                                    onMatchItemClick = onMatchItemClick,
                                    teamId = teamId,
                                    head2head = head2head,
                                    isHead2headLoading = isHead2headLoading
                                )
                            }
                        }
                    }
                }
            }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .systemBarsPadding(),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TeamInfoPreview1() {
    GoalPulseTheme {
        TeamInfoScreenContent(
            teamInfo = getMockTeam(),
            isLoading = false,
            onBackClick = {},
            colors = emptyMap(),
            onTeamInfoReload = {},
            isMatchesLoading = false,
            matchesComplete = getMockMatchesComplete(),
            matchesAhead = getMockMatchesAhead(),
            teamId = "-1",
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = Head2head(),
            isHead2headLoading = false,
            onMatchesReload = {},
            onPersonClick = {},
            news = News(),
            isLoadingNews = false
        )
    }
}

