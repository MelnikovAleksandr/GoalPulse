package ru.asmelnikov.competitions_main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competitions_main.components.CompetitionItem
import ru.asmelnikov.competitions_main.components.CompetitionsScaffoldTopBar
import ru.asmelnikov.competitions_main.components.LiquidPullToRefreshWrapper
import ru.asmelnikov.competitions_main.components.MainBackVideo
import ru.asmelnikov.competitions_main.components.SearchBarScrollState
import ru.asmelnikov.competitions_main.components.ShimmerListItem
import ru.asmelnikov.competitions_main.components.rememberSearchBarScrollState
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
                appState.navigate(route = Routes.Standings(it.compId, it.compUrl))
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
    onCompClick: (String, String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val videoBackdrop = rememberLayerBackdrop()
    val listBackdrop = rememberLayerBackdrop()
    val topBarBackdrop = rememberLayerBackdrop()
    val contentBackdrop = rememberCombinedBackdrop(videoBackdrop, listBackdrop)
    val refreshBackdrop = rememberCombinedBackdrop(videoBackdrop, listBackdrop, topBarBackdrop)
    var headerBottomY by remember { mutableFloatStateOf(0f) }
    var pullRefreshTopY by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val refreshIndicatorTopOffset by remember(density) {
        derivedStateOf {
            with(density) {
                (headerBottomY - pullRefreshTopY).coerceAtLeast(0f).toDp()
            }
        }
    }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchBarScrollState = rememberSearchBarScrollState()
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            searchBarScrollState.expand()
        }
    }

    LaunchedEffect(listState, searchBarScrollState) {
        var isFirstEmission = true
        snapshotFlow {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
            .distinctUntilChanged()
            .collect { isAtTop ->
                if (isFirstEmission) {
                    isFirstEmission = false
                } else if (isAtTop) {
                    searchBarScrollState.expand()
                }
            }
    }

    val focusManager = LocalFocusManager.current

    val filteredComps = remember(comps, searchQuery) {
        if (searchQuery.isBlank()) {
            comps
        } else {
            comps.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MainBackVideo(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(videoBackdrop),
            videoResId = R.raw.main_back_video,
            reverseVideoResId = R.raw.main_back_video_reverse
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                CompetitionsScaffoldTopBar(
                    modifier = Modifier
                        .layerBackdrop(topBarBackdrop)
                        .onGloballyPositioned { coordinates ->
                            headerBottomY = coordinates.positionInRoot().y + coordinates.size.height
                        },
                    backdrop = contentBackdrop,
                    competitionsCount = comps.size,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    searchBarScrollState = searchBarScrollState
                )
            }
        ) { paddingValues ->
            val listTopPadding = paddingValues.calculateTopPadding() + dimens.small3
            LiquidPullToRefreshWrapper(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        pullRefreshTopY = coordinates.positionInRoot().y
                    },
                backdrop = refreshBackdrop,
                isRefreshing = isLoading,
                onRefresh = updateComps,
                topOffset = refreshIndicatorTopOffset
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedContent(
                        modifier = Modifier.fillMaxSize(),
                        targetState = when {
                            isLoading && comps.isEmpty() -> ContentState.Loading
                            comps.isEmpty() -> ContentState.Empty
                            filteredComps.isEmpty() -> ContentState.NotFound
                            else -> ContentState.List
                        },
                        label = "competitions_content"
                    ) { state ->
                        when (state) {
                            ContentState.Loading -> {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .competitionsListScrollEffects(
                                            searchBarScrollState = searchBarScrollState,
                                            focusManager = focusManager,
                                            isScrollCollapseEnabled = searchQuery.isBlank()
                                        )
                                        .layerBackdrop(listBackdrop),
                                    verticalArrangement = Arrangement.spacedBy(dimens.medium2),
                                    contentPadding = PaddingValues(
                                        start = dimens.medium1,
                                        end = dimens.medium1,
                                        top = listTopPadding,
                                        bottom = paddingValues.calculateBottomPadding() + dimens.small3
                                    )
                                ) {
                                    items(10) {
                                        ShimmerListItem(backdrop = videoBackdrop)
                                    }
                                    item {
                                        Spacer(modifier = Modifier.navigationBarsPadding())
                                    }
                                }
                            }

                            ContentState.Empty -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = paddingValues.calculateTopPadding())
                                ) {
                                    EmptyContent(
                                        withScroll = true,
                                        onReloadClick = updateComps
                                    )
                                }
                            }

                            ContentState.NotFound -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = dimens.medium1)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            top = paddingValues.calculateTopPadding() + dimens.medium2
                                        ),
                                        text = stringResource(R.string.competitions_not_found),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White.copy(alpha = 0.55f)
                                    )
                                }
                            }

                            ContentState.List -> {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .competitionsListScrollEffects(
                                            searchBarScrollState = searchBarScrollState,
                                            focusManager = focusManager,
                                            isScrollCollapseEnabled = searchQuery.isBlank()
                                        )
                                        .layerBackdrop(listBackdrop),
                                    verticalArrangement = Arrangement.spacedBy(dimens.medium2),
                                    contentPadding = PaddingValues(
                                        start = dimens.medium1,
                                        end = dimens.medium1,
                                        top = listTopPadding,
                                        bottom = paddingValues.calculateBottomPadding() + dimens.small3
                                    )
                                ) {
                                    items(items = filteredComps, key = { it.id }) { comp ->
                                        CompetitionItem(
                                            modifier = Modifier.animateItem(),
                                            competition = comp,
                                            backdrop = videoBackdrop,
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            onCompClick = onCompClick
                                        )
                                    }
                                    item {
                                        Spacer(modifier = Modifier.navigationBarsPadding())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private enum class ContentState {
    Loading,
    Empty,
    NotFound,
    List,
}

@Composable
private fun Modifier.competitionsListScrollEffects(
    searchBarScrollState: SearchBarScrollState,
    focusManager: FocusManager,
    isScrollCollapseEnabled: Boolean,
): Modifier = nestedScroll(
    remember(searchBarScrollState, focusManager, isScrollCollapseEnabled) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && available.y != 0f) {
                    focusManager.clearFocus()
                    if (isScrollCollapseEnabled) {
                        searchBarScrollState.onScroll(-available.y)
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (isScrollCollapseEnabled) {
                    searchBarScrollState.snapToNearest()
                }
                return Velocity.Zero
            }
        }
    }
)

