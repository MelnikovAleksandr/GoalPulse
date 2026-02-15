package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingGif
import ru.asmelnikov.utils.getCompColor
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun FirstPagerScreenStandings(
    competitionStandings: CompetitionStandings?,
    seasons: List<String>,
    currentSeason: String,
    onSeasonUpdate: (String) -> Unit,
    isLoading: Boolean,
    onTeamClick: (Int) -> Unit,
    onReloadClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        SeasonDropDown(
            modifier = Modifier.fillMaxWidth(0.5f),
            onItemChanged = onSeasonUpdate,
            items = seasons,
            selectedItem = currentSeason
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.extraSmall1)
        ) {
            if (isLoading) LinearProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }

        when {
            isLoading && competitionStandings?.standings == null -> LoadingGif()
            !isLoading && competitionStandings?.standings == null -> EmptyContent(onReloadClick = onReloadClick)
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    competitionStandings?.standings?.forEachIndexed { standingIndex, standing ->
                        item {
                            Divider(color = MaterialTheme.colorScheme.primary)
                            StandingTopItem(
                                tableName = standing.group.ifEmpty { "Team" }
                            )
                            Divider(color = MaterialTheme.colorScheme.primary)
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
                            Divider(color = MaterialTheme.colorScheme.primary)
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