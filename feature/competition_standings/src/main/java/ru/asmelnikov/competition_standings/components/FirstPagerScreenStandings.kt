package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.getMockStandings
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.getCompColor
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun FirstPagerScreenStandings(
    competitionStandings: CompetitionStandings?,
    isLoading: Boolean,
    onTeamClick: (Int) -> Unit,
    onReloadClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(dimens.extraSmall1),
                color = MaterialTheme.colorScheme.primary
            )
        }
        AnimatedContent(targetState = competitionStandings?.standings == null) { emptyData ->
            when {
                isLoading && emptyData -> LoadingBall()
                !isLoading && emptyData -> EmptyContent(onReloadClick = onReloadClick)
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        competitionStandings?.standings?.forEachIndexed { standingIndex, standing ->
                            stickyHeader {
                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                                StandingTopItem(
                                    tableName = stringResource(standing.group.stringResId)
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            }
                            itemsIndexed(
                                items = standing.table,
                                key = { _, table -> table.team.id }) { tableIndex, table ->
                                val color = competitionStandings.competition.code.getCompColor(
                                    tableIndex,
                                    standing.table.size
                                )
                                StandingItem(
                                    modifier = Modifier.animateItem(),
                                    table = table,
                                    firstBoxColor = color,
                                    onTeamClick = onTeamClick
                                )
                                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            }
                            if (standingIndex == competitionStandings.standings.size - 1) {
                                item {
                                    BottomStandingItem()
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
private fun TablePreview1() {
    GoalPulseTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings(),
                isLoading = false,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TablePreview2() {
    GoalPulseTheme {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            FirstPagerScreenStandings(
                competitionStandings = getMockStandings(),
                isLoading = false,
                onTeamClick = {},
                onReloadClick = {}
            )
        }
    }
}