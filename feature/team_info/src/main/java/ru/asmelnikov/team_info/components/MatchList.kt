package ru.asmelnikov.team_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.Winner
import ru.asmelnikov.domain.models.getMockHead2Head
import ru.asmelnikov.domain.models.getMockMatchesAhead
import ru.asmelnikov.domain.models.getMockMatchesComplete
import ru.asmelnikov.utils.composables.MatchItem
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.lastRed

@Composable
fun MatchList(
    matches: List<Match>,
    topInset: Dp = 0.dp,
    isAhead: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean,
    teamId: String,
    calendarMatchIds: Set<Int> = emptySet(),
    calendarBusyMatchIds: Set<Int> = emptySet(),
    onCalendarClick: (Match) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = topInset)
    ) {
        itemsIndexed(
            items = matches,
            key = { _, match -> match.id }
        ) { index, match ->
            MatchItem(
                modifier = Modifier.animateItem(),
                isAhead = isAhead,
                expandedItemId = expandedItemId,
                onMatchItemClick = onMatchItemClick,
                isHead2headLoading = isHead2headLoading,
                matchId = match.id,
                homeTeamCrest = match.homeTeam.crest,
                homeName = match.homeTeam.shortName,
                awayTeamCrest = match.awayTeam.crest,
                awayName = match.awayTeam.shortName,
                bigDate = match.bigDate,
                fullDate = match.utcDate,
                homeScore = match.score.fullTime.home.toString(),
                awayScore = match.score.fullTime.away.toString(),
                numberOfMatches = head2head.aggregates.numberOfMatches,
                homeWinsPercentage = head2head.aggregates.homeWinsPercentage,
                drawsPercentage = head2head.aggregates.drawsPercentage,
                awayWinsPercentage = head2head.aggregates.awayWinsPercentage,
                homeWins = head2head.aggregates.homeTeam.wins,
                homeDraws = head2head.aggregates.homeTeam.draws,
                homeLosses = head2head.aggregates.homeTeam.losses,
                head2headId = head2head.id,
                compName = match.competition.name,
                color = getResultColor(match, teamId),
                isInCalendar = calendarMatchIds.contains(match.id),
                isCalendarLoading = calendarBusyMatchIds.contains(match.id),
                onCalendarClick = { onCalendarClick(match) }
            )

            if (index < matches.size - 1) {
                HorizontalDivider(color = MaterialTheme.colorScheme.primary)
            }
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun getResultColor(match: Match, teamId: String): Color {
    val isHomeTeam = teamId == match.homeTeam.id.toString()
    val isAwayTeam = teamId == match.awayTeam.id.toString()

    return when (match.score.winner) {
        Winner.HOME_TEAM -> if (isHomeTeam) MaterialTheme.colorScheme.primary else lastRed.copy(
            alpha = 0.5f
        )

        Winner.AWAY_TEAM -> if (isAwayTeam) MaterialTheme.colorScheme.primary else lastRed.copy(
            alpha = 0.5f
        )

        else -> MaterialTheme.colorScheme.secondary
    }
}

@Preview
@Composable
private fun MatchListPreview1() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MatchList(
                matches = getMockMatchesComplete(),
                isAhead = false,
                expandedItemId = -1,
                onMatchItemClick = {},
                head2head = getMockHead2Head(),
                isHead2headLoading = false,
                teamId = "66"
            )
        }
    }
}

@Preview
@Composable
private fun MatchListPreview2() {
    GoalPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MatchList(
                matches = getMockMatchesAhead(),
                isAhead = true,
                expandedItemId = -1,
                onMatchItemClick = {},
                head2head = getMockHead2Head(),
                isHead2headLoading = false,
                teamId = "66"
            )
        }
    }
}