package ru.asmelnikov.competition_standings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competition_standings.components.FirstPagerScreenStandings
import ru.asmelnikov.competition_standings.components.SecondPagerScreenScorers
import ru.asmelnikov.competition_standings.components.TabsStandings
import ru.asmelnikov.competition_standings.components.ThirdPagerScreenMatches
import ru.asmelnikov.competition_standings.components.Toolbar
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.getMockMatches
import ru.asmelnikov.domain.models.getMockScorers
import ru.asmelnikov.domain.models.getMockStandings
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.isPortrait
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTab
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTabs
import ru.asmelnikov.utils.composables.liquid.drawProgressivePlainBackdrop
import ru.asmelnikov.utils.composables.rememberMatchCalendarPermissionHandler
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigate
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SharedTransitionScope.CompetitionStandingsScreen(
    appState: MainAppState,
    compId: String,
    compUrl: String,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit,
    ) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CompetitionStandingsViewModel = koinViewModel(parameters = {
        parametersOf(
            compId,
            compUrl,
        )
    }),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val calendarPermissionHandler = rememberMatchCalendarPermissionHandler(
        onPermissionResult = viewModel::onCalendarPermissionResult,
    )
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionStandingSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null,
            ) {}

            is CompetitionStandingSideEffects.BackClick -> appState.popUp()

            is CompetitionStandingSideEffects.OnTeamInfoNavigate -> {
                appState.navigate(route = Routes.Team(it.teamId))
            }

            is CompetitionStandingSideEffects.OnPersonInfoNavigate -> {
                appState.navigate(route = Routes.Person(it.personId))
            }

            is CompetitionStandingSideEffects.RequestCalendarPermission -> {
                calendarPermissionHandler.launchSystemPermission()
            }

            is CompetitionStandingSideEffects.OpenCalendar -> {
                runCatching { context.startActivity(it.intent) }
                    .onFailure { viewModel.onCalendarInsertFailed() }
            }
        }
    }

    CompetitionStandingsContent(
        compUrl = state.compUrl,
        competitionStandings = state.competitionStandings,
        onBackClick = viewModel::onBackClick,
        isLoadingStandings = state.isLoadingStandings,
        isLoadingScorers = state.isLoadingScorers,
        scorers = state.scorers,
        matchesCompleted = state.matchesCompleted,
        matchesAhead = state.matchesAhead,
        isLoadingMatches = state.isLoadingMatches,
        expandedItemId = state.expandedItem,
        onMatchItemClick = viewModel::matchItemClick,
        head2head = state.head2head,
        isHead2headLoading = state.isHead2headLoading,
        onTeamClick = viewModel::onTeamClick,
        onReloadStandingsClick = viewModel::updateStandingsFromRemoteToLocal,
        onReloadMatchesClick = viewModel::updateMatchesFromRemoteToLocal,
        onReloadScorersClick = viewModel::updateScorersFromRemoteToLocal,
        onPersonClick = viewModel::onPersonClick,
        animatedVisibilityScope = animatedVisibilityScope,
        calendarMatchIds = state.calendarMatchIds,
        calendarBusyMatchIds = state.calendarBusyMatchIds,
        onCalendarClick = viewModel::onCalendarClick,
    )
}

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun SharedTransitionScope.CompetitionStandingsContent(
    compUrl: String,
    competitionStandings: CompetitionStandings,
    onBackClick: () -> Unit,
    isLoadingStandings: Boolean,
    scorers: List<Scorer>,
    isLoadingScorers: Boolean,
    matchesCompleted: List<MatchesByTour>,
    matchesAhead: List<MatchesByTour>,
    isLoadingMatches: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head = Head2head(),
    isHead2headLoading: Boolean = false,
    onTeamClick: (Int) -> Unit,
    onReloadStandingsClick: () -> Unit,
    onReloadScorersClick: () -> Unit,
    onReloadMatchesClick: () -> Unit,
    onPersonClick: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    calendarMatchIds: Set<Int> = emptySet(),
    calendarBusyMatchIds: Set<Int> = emptySet(),
    onCalendarClick: (Match) -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { TabsStandings.entries.count() },
    )
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    var blockOuterPagerScroll by remember { mutableStateOf(false) }
    var isPullActive by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = configuration.orientation) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            collapsingState.toolbarState.collapse()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = collapsingState,
            enabled = isPortrait() && !isPullActive,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                Toolbar(
                    collapsingState = collapsingState,
                    areaUrl = competitionStandings.area.flag,
                    compUrl = compUrl,
                    compName = competitionStandings.competition.name,
                    sharedTransitionScope = this@CompetitionStandingsContent,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onBackClick = onBackClick,
                )
            },
            body = {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets = WindowInsets(),
                    topBar = {
                        val blurRadiusPx = with(LocalDensity.current) { dimens.medium2.toPx() }
                        val tint = MaterialTheme.colorScheme.background
                        val tabTitles = TabsStandings.entries.map { stringResource(it.stringResId) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .drawProgressivePlainBackdrop(
                                        backdrop = backdrop,
                                        blurRadiusPx = blurRadiusPx,
                                        tint = tint,
                                    ),
                            )

                            LiquidBottomTabs(
                                pagerState = pagerState,
                                backdrop = backdrop,
                                tabsCount = tabTitles.size,
                                modifier = Modifier.padding(
                                    horizontal = dimens.medium2,
                                    vertical = dimens.small3,
                                ),
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    LiquidBottomTab({
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    }) {
                                        Text(
                                            text = title,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.labelMedium,
                                        )
                                    }
                                }
                            }
                        }
                    },
                ) { paddingValues ->
                    val topInset = paddingValues.calculateTopPadding()
                    HorizontalPager(
                        modifier = Modifier
                            .layerBackdrop(backdrop)
                            .fillMaxSize(),
                        state = pagerState,
                        beyondViewportPageCount = 1,
                        userScrollEnabled = !blockOuterPagerScroll,
                        verticalAlignment = Alignment.Top,
                    ) { page ->
                        when (page) {
                            0 -> {
                                FirstPagerScreenStandings(
                                    topInset = topInset,
                                    isPullToRefreshEnabled =
                                    !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    competitionStandings = competitionStandings,
                                    isLoading = isLoadingStandings,
                                    onTeamClick = onTeamClick,
                                    onReloadClick = onReloadStandingsClick,
                                    onPullActiveChange = { isPullActive = it },
                                )
                            }

                            1 -> {
                                SecondPagerScreenScorers(
                                    scorers = scorers,
                                    isPullToRefreshEnabled =
                                    !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    topInset = topInset,
                                    isLoadingScorers = isLoadingScorers,
                                    onReloadClick = onReloadScorersClick,
                                    onPersonClick = onPersonClick,
                                    onPullActiveChange = { isPullActive = it },
                                )
                            }

                            2 -> {
                                ThirdPagerScreenMatches(
                                    matchesCompleted = matchesCompleted,
                                    isPullToRefreshEnabled =
                                    !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    topInset = topInset,
                                    matchesAhead = matchesAhead,
                                    isLoadingMatches = isLoadingMatches,
                                    expandedItemId = expandedItemId,
                                    onMatchItemClick = onMatchItemClick,
                                    head2head = head2head,
                                    isHead2headLoading = isHead2headLoading,
                                    onReloadClick = onReloadMatchesClick,
                                    calendarMatchIds = calendarMatchIds,
                                    calendarBusyMatchIds = calendarBusyMatchIds,
                                    onCalendarClick = onCalendarClick,
                                    onOuterPagerScrollBlocked = { blockOuterPagerScroll = it },
                                    onPullActiveChange = { isPullActive = it },
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}

@Preview(
    showBackground = true,
    locale = "ru",
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun StandingsPreview1() {
    GoalPulseTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            AnimatedVisibility(visible = true) {
                CompetitionStandingsContent(
                    compUrl = "123",
                    competitionStandings = getMockStandings(),
                    onBackClick = {},
                    isLoadingStandings = false,
                    scorers = getMockScorers(),
                    isLoadingScorers = false,
                    matchesCompleted = getMockMatches().matchesByTourCompleted,
                    matchesAhead = getMockMatches().matchesByTourAhead,
                    isLoadingMatches = false,
                    expandedItemId = -1,
                    onMatchItemClick = {},
                    isHead2headLoading = false,
                    onTeamClick = {},
                    onReloadStandingsClick = {},
                    onReloadScorersClick = {},
                    onReloadMatchesClick = {},
                    onPersonClick = {},
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun StandingsPreview2() {
    GoalPulseTheme(darkTheme = false) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            AnimatedVisibility(visible = true) {
                CompetitionStandingsContent(
                    compUrl = "123",
                    competitionStandings = getMockStandings(),
                    onBackClick = {},
                    isLoadingStandings = false,
                    scorers = getMockScorers(),
                    isLoadingScorers = false,
                    matchesCompleted = getMockMatches().matchesByTourCompleted,
                    matchesAhead = getMockMatches().matchesByTourAhead,
                    isLoadingMatches = false,
                    expandedItemId = -1,
                    onMatchItemClick = {},
                    isHead2headLoading = false,
                    onTeamClick = {},
                    onReloadStandingsClick = {},
                    onReloadScorersClick = {},
                    onReloadMatchesClick = {},
                    onPersonClick = {},
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}
