package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingGif
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
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

    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        2
    }

    val brushColor: List<Color> = if (isMaterialColors)
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    else
        listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))

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
        Box(
            modifier = Modifier
                .background(
                    if (isMaterialColors) MaterialTheme.colorScheme.background else itemColor
                )
                .fillMaxWidth()
                .height(MaterialTheme.dimens.extraSmall1)
        ) {
            if (isLoading) LinearProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = if (isMaterialColors) MaterialTheme.colorScheme.background else itemColor
            )
        }

        when {
            isLoading && matchesCompleted.isEmpty() && matchesAhead.isEmpty() -> LoadingGif()
            !isLoading && matchesCompleted.isEmpty() && matchesAhead.isEmpty() -> EmptyContent(
                onReloadClick = onReloadClick
            )

            else -> {

                AnimatedVisibility(
                    visible = matchesAhead.isEmpty() && matchesCompleted.isNotEmpty(),
                ) {
                    MatchList(
                        matchesCompleted,
                        matchesAhead,
                        isAhead = false,
                        expandedItemId = expandedItemId,
                        onMatchItemClick = onMatchItemClick,
                        head2head = head2head,
                        isHead2headLoading = isHead2headLoading,
                        teamId = teamId
                    )
                }
                AnimatedVisibility(
                    visible = matchesAhead.isNotEmpty() && matchesCompleted.isEmpty(),
                ) {
                    MatchList(
                        matchesCompleted,
                        matchesAhead,
                        isAhead = true,
                        expandedItemId = expandedItemId,
                        onMatchItemClick = onMatchItemClick,
                        head2head = head2head,
                        isHead2headLoading = isHead2headLoading,
                        teamId = teamId
                    )
                }
                AnimatedVisibility(
                    visible = matchesAhead.isNotEmpty() && matchesCompleted.isNotEmpty(),
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        PagerTabRow(
                            modifier = Modifier.fillMaxWidth(),
                            tabTitles = listOf("Completed", "Ahead"),
                            selectedIndex = pagerState.currentPage,
                            onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                            pagerState = pagerState,
                            containerColor = if (isMaterialColors) MaterialTheme.colorScheme.background else itemColor

                        )

                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = pagerState,
                            beyondViewportPageCount = 2,
                            verticalAlignment = Alignment.Top
                        ) { page ->
                            when (page) {
                                0 -> {
                                    MatchList(
                                        matchesCompleted,
                                        matchesAhead,
                                        isAhead = false,
                                        expandedItemId = expandedItemId,
                                        onMatchItemClick = onMatchItemClick,
                                        head2head = head2head,
                                        isHead2headLoading = isHead2headLoading,
                                        teamId = teamId
                                    )
                                }

                                1 -> {
                                    MatchList(
                                        matchesCompleted,
                                        matchesAhead,
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