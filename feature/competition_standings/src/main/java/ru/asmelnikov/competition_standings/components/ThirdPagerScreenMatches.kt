package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.getMockMatches
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.TabsMatches
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTab
import ru.asmelnikov.utils.composables.liquid.LiquidBottomTabs
import ru.asmelnikov.utils.composables.liquid.LiquidPullToRefreshWrapper
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun ThirdPagerScreenMatches(
    matchesCompleted: List<MatchesByTour>,
    topInset: Dp,
    isPullToRefreshEnabled: Boolean,
    matchesAhead: List<MatchesByTour>,
    isLoadingMatches: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head = Head2head(),
    isHead2headLoading: Boolean = false,
    onReloadClick: () -> Unit,
    calendarMatchIds: Set<Int> = emptySet(),
    calendarBusyMatchIds: Set<Int> = emptySet(),
    onCalendarClick: (Match) -> Unit = {},
    onOuterPagerScrollBlocked: (Boolean) -> Unit = {},
    onPullActiveChange: (Boolean) -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    val scope = rememberCoroutineScope()
    val tabListState by remember(matchesCompleted, matchesAhead) {
        mutableStateOf(
            buildList {
                if (matchesCompleted.isNotEmpty()) add(TabsMatches.Completed)
                if (matchesAhead.isNotEmpty()) add(TabsMatches.Ahead)
            },
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        tabListState.count()
    }

    AnimatedContent(
        targetState = matchesCompleted.isEmpty() && matchesAhead.isEmpty(),
        modifier = Modifier.fillMaxSize(),
    ) { emptyState ->
        if (emptyState && !isLoadingMatches) {
            EmptyContent(
                modifier = Modifier.padding(top = topInset),
                onReloadClick = onReloadClick,
            )
        } else {
            LiquidPullToRefreshWrapper(
                modifier = Modifier.fillMaxSize(),
                backdrop = backdrop,
                isRefreshing = isLoadingMatches,
                onRefresh = onReloadClick,
                enabled = isPullToRefreshEnabled,
                onPullActiveChange = onPullActiveChange,
                topOffset = topInset,
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(),
                    topBar = {
                        Column {
                            AnimatedVisibility(visible = tabListState.count() > 1) {
                                val tabTitles =
                                    tabListState.map { stringResource(it.stringResId) }
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
                                    contentAlignment = Alignment.Center,
                                ) {
                                    LiquidBottomTabs(
                                        pagerState = pagerState,
                                        backdrop = backdrop,
                                        heightMain = 44f.dp,
                                        heightInner = 38f.dp,
                                        tabsCount = tabTitles.size,
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .padding(top = topInset, bottom = dimens.small3),
                                    ) {
                                        tabTitles.forEachIndexed { index, title ->
                                            LiquidBottomTab({
                                                scope.launch {
                                                    pagerState.animateScrollToPage(index)
                                                }
                                            }) {
                                                Text(
                                                    text = title,
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    style = MaterialTheme.typography.labelSmall,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
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
                        verticalAlignment = Alignment.Top,
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
                                    isHead2headLoading = isHead2headLoading,
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
                                    isHead2headLoading = isHead2headLoading,
                                    calendarMatchIds = calendarMatchIds,
                                    calendarBusyMatchIds = calendarBusyMatchIds,
                                    onCalendarClick = onCalendarClick,
                                )
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
            isPullToRefreshEnabled = true,
            isLoadingMatches = false,
            topInset = Dp.Hairline,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview2() {
    GoalPulseTheme(darkTheme = true) {
        ThirdPagerScreenMatches(
            matchesCompleted = emptyList(),
            matchesAhead = emptyList(),
            isPullToRefreshEnabled = true,
            isLoadingMatches = false,
            topInset = Dp.Hairline,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview3() {
    GoalPulseTheme {
        ThirdPagerScreenMatches(
            matchesCompleted = getMockMatches().matchesByTourCompleted,
            matchesAhead = emptyList(),
            isPullToRefreshEnabled = true,
            topInset = Dp.Hairline,
            isLoadingMatches = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview4() {
    GoalPulseTheme(darkTheme = true) {
        ThirdPagerScreenMatches(
            matchesCompleted = emptyList(),
            topInset = Dp.Hairline,
            isPullToRefreshEnabled = true,
            matchesAhead = getMockMatches().matchesByTourAhead,
            isLoadingMatches = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
        )
    }
}
