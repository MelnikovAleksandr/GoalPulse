package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.getMockStandings
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.liquid.LiquidPullToRefreshWrapper
import ru.asmelnikov.utils.composables.stickyHeaderContentPaddingAware
import ru.asmelnikov.utils.getCompColor
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@Composable
fun FirstPagerScreenStandings(
    competitionStandings: CompetitionStandings,
    topInset: Dp,
    isPullToRefreshEnabled: Boolean,
    isLoading: Boolean,
    onTeamClick: (Int) -> Unit,
    onReloadClick: () -> Unit,
    onPullActiveChange: (Boolean) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    AnimatedContent(
        targetState = competitionStandings.standings.isEmpty(),
        modifier = Modifier.fillMaxSize()
    ) { emptyState ->
        if (emptyState && !isLoading) {
            EmptyContent(
                modifier = Modifier.padding(top = topInset),
                onReloadClick = onReloadClick
            )
        } else {
            LiquidPullToRefreshWrapper(
                modifier = Modifier.fillMaxSize(),
                backdrop = backdrop,
                isRefreshing = isLoading,
                onRefresh = onReloadClick,
                enabled = isPullToRefreshEnabled,
                onPullActiveChange = onPullActiveChange,
                topOffset = topInset
            ) {
                LazyColumn(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .background(backgroundColor)
                        .fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(top = topInset)
                ) {
                    competitionStandings.standings.forEachIndexed { standingIndex, standing ->
                        val stickyKey = "standing_sticky_$standingIndex"
                        stickyHeaderContentPaddingAware(
                            listState = listState,
                            key = stickyKey,
                            contentType = stickyKey
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(backgroundColor)
                            ) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                                StandingTopItem(
                                    tableName = stringResource(standing.group.stringResId)
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        itemsIndexed(
                            items = standing.table,
                            key = { _, table -> table.team.id }
                        ) { tableIndex, table ->
                            val color = competitionStandings.competition.code.getCompColor(
                                tableIndex,
                                standing.table.size
                            )
                            StandingItem(
                                modifier = Modifier,
                                table = table,
                                firstBoxColor = color,
                                onTeamClick = onTeamClick
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                        }
                        if (standingIndex == competitionStandings.standings.lastIndex) {
                            item { BottomStandingItem() }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview1() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings(),
                isLoading = true,
                isPullToRefreshEnabled = true,
                topInset = Dp.Hairline,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview2() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings().copy(standings = emptyList()),
                isLoading = false,
                isPullToRefreshEnabled = true,
                topInset = Dp.Hairline,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview3() {
    GoalPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings(),
                isLoading = true,
                isPullToRefreshEnabled = false,
                topInset = Dp.Hairline,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}