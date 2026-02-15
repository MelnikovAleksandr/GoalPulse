package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchList(
    matchesCompleted: List<MatchesByTour>,
    matchesAhead: List<MatchesByTour>,
    isAhead: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val list = if (!isAhead) matchesCompleted else matchesAhead
        list.forEach { matchesByTour ->
            stickyHeader {
                StickyHeader(matchesByTour = matchesByTour)
            }
            itemsIndexed(
                items = matchesByTour.matches,
                key = { _, match -> match.id }
            ) { index, match ->
                MatchItem(
                    modifier = Modifier.animateItem(),
                    match = match,
                    isAhead = isAhead,
                    expandedItemId = expandedItemId,
                    onMatchItemClick = onMatchItemClick,
                    head2head = head2head,
                    isHead2headLoading = isHead2headLoading
                )
                if (index < matchesByTour.matches.size - 1) {
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}