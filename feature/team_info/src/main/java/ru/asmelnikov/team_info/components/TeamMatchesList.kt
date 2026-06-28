package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.getMockHead2Head
import ru.asmelnikov.domain.models.getMockMatchesAhead
import ru.asmelnikov.domain.models.getMockMatchesComplete
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.composables.TabsMatches
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TeamMatchesList(
    matchesCompleted: List<Match>,
    matchesAhead: List<Match>,
    isLoading: Boolean,
    onReloadClick: () -> Unit,
    isMaterialColors: Boolean,
    stickyHeaderColor: Color,
    itemColor: Color,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean,
    teamId: String
) {

    val scope = rememberCoroutineScope()
    val scheme = MaterialTheme.colorScheme
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

    val brushColor = remember(isMaterialColors, stickyHeaderColor, itemColor) {
        if (isMaterialColors)
            listOf(scheme.background, scheme.background)
        else
            listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = brushColor,
                    startY = 0f
                )
            )
    ) {

        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.extraSmall1),
                color = if (isMaterialColors) MaterialTheme.colorScheme.background else itemColor
            )
        }

        AnimatedContent(targetState = matchesCompleted.isEmpty() && matchesAhead.isEmpty()) { emptyData ->
            when {
                isLoading && emptyData -> LoadingBall()
                !isLoading && emptyData -> EmptyContent(onReloadClick = onReloadClick)
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = tabListState.count() > 1) {
                            PagerTabRow(
                                modifier = Modifier.fillMaxWidth(),
                                tabTitles = tabListState.map { stringResource(it.stringResId) },
                                selectedIndex = pagerState.currentPage,
                                onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                                containerColor = if (isMaterialColors) MaterialTheme.colorScheme.background else itemColor
                            )
                        }

                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = pagerState,
                            beyondViewportPageCount = 1,
                            verticalAlignment = Alignment.Top
                        ) { page ->
                            when (tabListState[page]) {
                                TabsMatches.Completed -> {
                                    MatchList(
                                        matches = matchesCompleted,
                                        isAhead = false,
                                        expandedItemId = expandedItemId,
                                        onMatchItemClick = onMatchItemClick,
                                        head2head = head2head,
                                        isHead2headLoading = isHead2headLoading,
                                        teamId = teamId
                                    )
                                }

                                TabsMatches.Ahead -> {
                                    MatchList(
                                        matches = matchesAhead,
                                        isAhead = true,
                                        expandedItemId = expandedItemId,
                                        onMatchItemClick = onMatchItemClick,
                                        head2head = head2head,
                                        isHead2headLoading = isHead2headLoading,
                                        teamId = teamId
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
        TeamMatchesList(
            matchesCompleted = getMockMatchesComplete(),
            matchesAhead = getMockMatchesAhead(),
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
            isLoading = false,
            isMaterialColors = true,
            stickyHeaderColor = Color.White,
            itemColor = Color.White,
            head2head = getMockHead2Head(),
            teamId = "66"
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview2() {
    GoalPulseTheme {
        TeamMatchesList(
            matchesCompleted = getMockMatchesComplete(),
            matchesAhead = emptyList(),
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
            isLoading = false,
            isMaterialColors = true,
            stickyHeaderColor = Color.White,
            itemColor = Color.White,
            head2head = getMockHead2Head(),
            teamId = "66"
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchesPreview3() {
    GoalPulseTheme(darkTheme = true) {
        TeamMatchesList(
            matchesCompleted = emptyList(),
            matchesAhead = getMockMatchesAhead(),
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            onReloadClick = {},
            isLoading = false,
            isMaterialColors = true,
            stickyHeaderColor = Color.White,
            itemColor = Color.White,
            head2head = getMockHead2Head(),
            teamId = "66"
        )
    }
}