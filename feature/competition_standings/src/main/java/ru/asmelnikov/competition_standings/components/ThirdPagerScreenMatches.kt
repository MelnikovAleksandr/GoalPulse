package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import kotlinx.coroutines.launch
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingGif
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThirdPagerScreenMatches(
    matchesCompleted: List<MatchesByTour>,
    matchesAhead: List<MatchesByTour>,
    seasons: List<String>,
    currentSeasonMatches: String,
    onSeasonMatchesUpdate: (String) -> Unit,
    isLoadingMatches: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head = Head2head(),
    isHead2headLoading: Boolean = false,
    onReloadClick: () -> Unit
) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        2
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SeasonDropDown(
            modifier = Modifier.fillMaxWidth(0.5f),
            onItemChanged = onSeasonMatchesUpdate,
            items = seasons,
            selectedItem = currentSeasonMatches
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.extraSmall1)
        ) {
            if (isLoadingMatches) LinearProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }
        when {
            isLoadingMatches && matchesCompleted.isEmpty() && matchesAhead.isEmpty() -> LoadingGif()
            !isLoadingMatches && matchesCompleted.isEmpty() && matchesAhead.isEmpty() -> EmptyContent(onReloadClick = onReloadClick)
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
                        isHead2headLoading = isHead2headLoading
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
                        isHead2headLoading = isHead2headLoading
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
                            pagerState = pagerState
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
                                        isHead2headLoading = isHead2headLoading
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