package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.getMockMatches
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.composables.TabsMatches
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTab
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTabs
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun ThirdPagerScreenMatches(
    matchesCompleted: List<MatchesByTour>,
    paddingValues: PaddingValues = PaddingValues(),
    matchesAhead: List<MatchesByTour>,
    isLoadingMatches: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head = Head2head(),
    isHead2headLoading: Boolean = false,
    onReloadClick: () -> Unit,
    onOuterPagerScrollBlocked: (Boolean) -> Unit = {}
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    val scope = rememberCoroutineScope()
    val topInset = paddingValues.calculateTopPadding()
    val tabListState by remember(matchesCompleted, matchesAhead) {
        mutableStateOf(
            buildList {
                if (matchesCompleted.isNotEmpty()) add(TabsMatches.Completed)
                if (matchesAhead.isNotEmpty()) add(TabsMatches.Ahead)
            }
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        tabListState.count()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        AnimatedContent(targetState = matchesCompleted.isEmpty() && matchesAhead.isEmpty()) { emptyData ->
            when {
                isLoadingMatches && emptyData -> LoadingBall(modifier = Modifier.padding(top = topInset))
                !isLoadingMatches && emptyData -> EmptyContent(
                    modifier = Modifier.padding(top = topInset),
                    onReloadClick = onReloadClick
                )

                else -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets(),
                        topBar = {
                            Column {
                                AnimatedVisibility(visible = tabListState.count() > 1) {
                                    val tabTitles =
                                        tabListState.map { stringResource(it.stringResId) }
                                    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

                                    LaunchedEffect(pagerState.currentPage) {
                                        snapshotFlow { pagerState.currentPage }.collect { page ->
                                            selectedTabIndex = page
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .pointerInput(Unit) {
                                                awaitEachGesture {
                                                    awaitFirstDown(requireUnconsumed = false)
                                                    onOuterPagerScrollBlocked(true)
                                                    do {
                                                        val event =
                                                            awaitPointerEvent(PointerEventPass.Final)
                                                    } while (event.changes.any { it.pressed })
                                                    onOuterPagerScrollBlocked(false)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LiquidBottomTabs(
                                            selectedTabIndex = { selectedTabIndex },
                                            onTabSelected = {
                                                selectedTabIndex = it
                                                if (pagerState.currentPage != it) {
                                                    scope.launch { pagerState.animateScrollToPage(it) }
                                                }
                                            },
                                            backdrop = backdrop,
                                            heightMain = 44f.dp,
                                            heightInner = 38f.dp,
                                            tabsCount = tabTitles.size,
                                            modifier = Modifier
                                                .fillMaxWidth(0.7f)
                                                .padding(top = topInset, bottom = dimens.small3)
                                        ) {
                                            tabTitles.forEachIndexed { index, title ->
                                                LiquidBottomTab({ selectedTabIndex = index }) {
                                                    Text(
                                                        text = title,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        style = MaterialTheme.typography.labelSmall
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                AnimatedVisibility(visible = isLoadingMatches) {
                                    LinearProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(
                                                top = if (tabListState.count() > 1) 0.dp else topInset
                                            )
                                            .height(dimens.extraSmall1),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.background
                                    )
                                }
                            }
                        }
                    ) { innerPaddingValues ->
                        val innerTopInset = innerPaddingValues.calculateTopPadding()
                        val topInsetResult =
                            if (tabListState.count() > 1) innerTopInset else topInset
                        HorizontalPager(
                            modifier = Modifier
                                .layerBackdrop(backdrop)
                                .fillMaxSize(),
                            state = pagerState,
                            beyondViewportPageCount = 1,
                            verticalAlignment = Alignment.Top
                        ) { page ->
                            when (tabListState[page]) {
                                TabsMatches.Completed -> {
                                    MatchList(
                                        matches = matchesCompleted,
                                        topInset = topInsetResult,
                                        isAhead = false,
                                        expandedItemId = expandedItemId,
                                        onMatchItemClick = onMatchItemClick,
                                        head2head = head2head,
                                        isHead2headLoading = isHead2headLoading
                                    )
                                }

                                TabsMatches.Ahead -> {
                                    MatchList(
                                        matches = matchesAhead,
                                        topInset = topInsetResult,
                                        isAhead = true,
                                        expandedItemId = expandedItemId,
                                        onMatchItemClick = onMatchItemClick,
                                        head2head = head2head,
                                        isHead2headLoading = isHead2headLoading
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview1() {
    GoalPulseTheme(darkTheme = true) {
        ThirdPagerScreenMatches(
            matchesCompleted = getMockMatches().matchesByTourCompleted,
            matchesAhead = getMockMatches().matchesByTourAhead,
            isLoadingMatches = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {}
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview2() {
    GoalPulseTheme {
        ThirdPagerScreenMatches(
            matchesCompleted = getMockMatches().matchesByTourCompleted,
            matchesAhead = emptyList(),
            isLoadingMatches = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {}
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview3() {
    GoalPulseTheme(darkTheme = true) {
        ThirdPagerScreenMatches(
            matchesCompleted = emptyList(),
            matchesAhead = getMockMatches().matchesByTourAhead,
            isLoadingMatches = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {}
        )
    }
}