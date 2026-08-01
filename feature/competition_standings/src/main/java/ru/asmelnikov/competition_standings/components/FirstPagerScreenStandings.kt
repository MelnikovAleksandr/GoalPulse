package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.getMockStandings
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.getCompColor
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

private enum class OverlayState {
    Loading,
    Empty,
    None
}

@Composable
fun FirstPagerScreenStandings(
    competitionStandings: CompetitionStandings,
    topInset: Dp,
    isLoading: Boolean,
    onTeamClick: (Int) -> Unit,
    onReloadClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val overlayState = remember(competitionStandings, isLoading) {
        when {
            isLoading && competitionStandings.standings.isEmpty() -> OverlayState.Loading
            !isLoading && competitionStandings.standings.isEmpty() -> OverlayState.Empty
            else -> OverlayState.None
        }
    }
    Box(
        modifier = Modifier
            .padding(top = topInset)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = listState
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
                            .background(MaterialTheme.colorScheme.background)
                    ) {

                        AnimatedVisibility(visible = isLoading && standingIndex == 0) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimens.extraSmall1),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.background
                            )
                        }

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
        AnimatedContent(
            targetState = overlayState,
            modifier = Modifier.fillMaxSize()
        ) { state ->
            when (state) {
                OverlayState.Loading -> LoadingBall()
                OverlayState.Empty -> EmptyContent(onReloadClick = onReloadClick)
                OverlayState.None -> Box(modifier = Modifier.fillMaxSize())
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
                isLoading = false,
                topInset = Dp.Unspecified,
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
                isLoading = true,
                topInset = Dp.Unspecified,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview3() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings().copy(standings = emptyList()),
                isLoading = false,
                topInset = Dp.Unspecified,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview4() {
    GoalPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings(),
                isLoading = false,
                topInset = Dp.Unspecified,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}