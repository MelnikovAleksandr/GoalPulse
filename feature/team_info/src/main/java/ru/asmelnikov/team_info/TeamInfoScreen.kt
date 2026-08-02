package ru.asmelnikov.team_info

import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.kmpalette.color
import com.kmpalette.rememberPaletteState
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

    val context = LocalContext.current
    val paletteState = rememberPaletteState()

    LaunchedEffect(teamInfo.crest) {
        if (teamInfo.crest.isBlank()) return@LaunchedEffect
        val result = ImageLoader(context).execute(
            ImageRequest.Builder(context)
                .data(teamInfo.crest)
                .allowHardware(false)
                .build()
        )
        val bitmap = (result as? SuccessResult)
            ?.drawable
            ?.let { (it as BitmapDrawable).bitmap }
            ?.asImageBitmap()
            ?: return@LaunchedEffect
        paletteState.generate(bitmap)
    }

    val topColors = paletteState.palette
        ?.swatches
        ?.sortedByDescending { it.population }
        ?.take(3)
        ?.map { it.color }
        ?: emptyList()

    val defaultColor = MaterialTheme.colorScheme.background

    val color1 by animateColorAsState(
        targetValue = topColors.getOrElse(0) { defaultColor },
        animationSpec = tween(durationMillis = 500),
        label = "color1"
    )

    val color2 by animateColorAsState(
        targetValue = topColors.getOrElse(1) { color1 },
        animationSpec = tween(durationMillis = 500),
        label = "color2"
    )

    val color3 by animateColorAsState(
        targetValue = topColors.getOrElse(2) { color1 },
        animationSpec = tween(durationMillis = 500),
        label = "color3"
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(color1, color2, color3)
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
                    mainColor = color1,
                    secondColor = color2,
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
                                        tint = color2
                                    )
                            )

                            LiquidBottomTabs(
                                pagerState = pagerState,
                                backdrop = backdrop,
                                tabsCount = tabTitles.size,
                                background = color2,
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
                                    itemColor = color2,
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
                                    color = color2,
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

