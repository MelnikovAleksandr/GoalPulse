package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.getMockHead2Head
import ru.asmelnikov.domain.models.getMockMatches
import ru.asmelnikov.utils.composables.MatchItem
import ru.asmelnikov.utils.composables.stickyHeaderContentPaddingAware
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@Composable
fun MatchList(
    matches: List<MatchesByTour>,
    topInset: Dp = 0.dp,
    isAhead: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean,
    calendarMatchIds: Set<Int> = emptySet(),
    calendarBusyMatchIds: Set<Int> = emptySet(),
    onCalendarClick: (Match) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = listState,
        contentPadding = PaddingValues(top = topInset),
    ) {
        matches.forEachIndexed { index, matchesByTour ->
            val stickyKey = "standing_sticky_$index"
            stickyHeaderContentPaddingAware(
                listState = listState,
                key = stickyKey,
                contentType = stickyKey,
            ) {
                StickyHeader(matchesByTour = matchesByTour)
            }
            itemsIndexed(
                items = matchesByTour.matches,
                key = { _, match -> match.id },
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
                    isInCalendar = calendarMatchIds.contains(match.id),
                    isCalendarLoading = calendarBusyMatchIds.contains(match.id),
                    onCalendarClick = { onCalendarClick(match) },
                )
                if (index < matchesByTour.matches.size - 1) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
private fun MatchListPreview1() {
    GoalPulseTheme(darkTheme = true) {
        MatchList(
            matches = getMockMatches().matchesByTourCompleted,
            isAhead = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = getMockHead2Head(),
            isHead2headLoading = false,
        )
    }
}

@Preview
@Composable
private fun MatchListPreview2() {
    GoalPulseTheme {
        MatchList(
            matches = getMockMatches().matchesByTourAhead,
            isAhead = true,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = getMockHead2Head(),
            isHead2headLoading = false,
        )
    }
}
