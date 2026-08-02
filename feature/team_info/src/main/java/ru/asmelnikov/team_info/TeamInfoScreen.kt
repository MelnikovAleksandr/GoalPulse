package ru.asmelnikov.team_info

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
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
import ru.asmelnikov.utils.composables.isPortrait
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTab
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTabs
import ru.asmelnikov.utils.composables.liquid.drawProgressivePlainBackdrop
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
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
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
    var isPullActive by remember { mutableStateOf(false) }
    var blockOuterPagerScroll by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = configuration.orientation) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            collapsingState.toolbarState.collapse()
        }
    }
    LaunchedEffect(key1 = colors) {
        if (colors.isNotEmpty()) {
            vibrant = colors["vibrant"] ?: ""
            lightMutedSwatch = colors["mutedSwatch"] ?: ""
            onDarkVibrant = colors["onDarkVibrant"] ?: ""
        }
    }

    val defaultColor = MaterialTheme.colorScheme.background
    val targetVibrant = Color(
        vibrant.ifBlank { "#ffffff" }.toColorInt()
    ).takeIf { vibrant.isNotBlank() } ?: defaultColor
    val targetLightMuted = Color(
        lightMutedSwatch.ifBlank { "#ffffff" }.toColorInt()
    ).takeIf { lightMutedSwatch.isNotBlank() } ?: defaultColor
    val targetOnDarkVibrant = Color(
        onDarkVibrant.ifBlank { "#ffffff" }.toColorInt()
    ).takeIf { onDarkVibrant.isNotBlank() } ?: defaultColor
    val animatedVibrant by animateColorAsState(
        targetValue = if (colors.isNotEmpty()) targetVibrant else defaultColor,
        animationSpec = tween(durationMillis = 700),
        label = "vibrant"
    )
    val animatedLightMuted by animateColorAsState(
        targetValue = if (colors.isNotEmpty()) targetLightMuted else defaultColor,
        animationSpec = tween(durationMillis = 700),
        label = "lightMuted"
    )
    val animatedOnDarkVibrant by animateColorAsState(
        targetValue = if (colors.isNotEmpty()) targetOnDarkVibrant else defaultColor,
        animationSpec = tween(durationMillis = 700),
        label = "onDarkVibrant"
    )
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            animatedVibrant,
            animatedLightMuted,
            animatedOnDarkVibrant
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = collapsingState,
            enabled = isPortrait() && !isPullActive,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                Toolbar(
                    collapsingState = collapsingState,
                    mainColor = animatedVibrant,
                    secondColor = animatedLightMuted,
                    teamName = teamInfo.name,
                    teamCrest = teamInfo.crest,
                    onBackClick = onBackClick
                )
            },
            body = {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(),
                    topBar = {
                        val blurRadiusPx = with(LocalDensity.current) { dimens.medium2.toPx() }

                        val tabTitles = TabsTeam.entries.map { stringResource(it.stringResId) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .drawProgressivePlainBackdrop(
                                        backdrop = backdrop,
                                        blurRadiusPx = blurRadiusPx,
                                        tint = animatedLightMuted
                                    )
                            )

                            LiquidBottomTabs(
                                pagerState = pagerState,
                                backdrop = backdrop,
                                tabsCount = tabTitles.size,
                                background = animatedLightMuted,
                                modifier = Modifier.padding(
                                    horizontal = dimens.medium2,
                                    vertical = dimens.small3
                                )
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    LiquidBottomTab({
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    }) {
                                        Text(
                                            text = title,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    val topInset = paddingValues.calculateTopPadding()
                    HorizontalPager(
                        modifier = Modifier
                            .layerBackdrop(backdrop)
                            .background(gradientBrush)
                            .fillMaxSize(),
                        state = pagerState,
                        beyondViewportPageCount = 1,
                        userScrollEnabled = !blockOuterPagerScroll,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        when (page) {
                            0 -> {
                                SquadPagerList(
                                    teamInfo = teamInfo,
                                    topInset = topInset,
                                    isPullToRefreshEnabled = !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    isLoading = isLoading,
                                    itemColor = animatedLightMuted,
                                    isMaterialColors = isMaterialColors,
                                    onReloadClick = onTeamInfoReload,
                                    onPersonClick = onPersonClick,
                                    onPullActiveChange = { isPullActive = it }
                                )
                            }

                            1 -> {
                                TeamInfoPage(
                                    teamInfo = teamInfo,
                                    topInset = topInset,
                                    isPullToRefreshEnabled = !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    isLoading = isLoading,
                                    onReloadClick = onTeamInfoReload,
                                    news = news,
                                    isLoadingNews = isLoadingNews,
                                    onPullActiveChange = { isPullActive = it }
                                )
                            }

                            2 -> {
                                TeamMatchesList(
                                    matchesCompleted = matchesComplete,
                                    matchesAhead = matchesAhead,
                                    isLoading = isMatchesLoading,
                                    topInset = topInset,
                                    isPullToRefreshEnabled = !isPortrait() || collapsingState.toolbarState.progress == 1f,
                                    onReloadClick = onMatchesReload,
                                    expandedItemId = expandedItemId,
                                    onMatchItemClick = onMatchItemClick,
                                    teamId = teamId,
                                    head2head = head2head,
                                    isHead2headLoading = isHead2headLoading,
                                    onOuterPagerScrollBlocked = { blockOuterPagerScroll = it },
                                    onPullActiveChange = { isPullActive = it }
                                )
                            }
                        }
                    }
                }
            }
        )
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

