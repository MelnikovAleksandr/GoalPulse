package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun StickyHeader(
    modifier: Modifier = Modifier,
    matchesByTour: MatchesByTour
) {

    val text = when {
        matchesByTour.matchday == -1 -> matchesByTour.matches.firstOrNull()?.stage.toString()
        matchesByTour.seasonType == "CUP" -> "${matchesByTour.matchday} tour ${matchesByTour.stage}"
        else -> "${matchesByTour.matchday} tour"
    }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.padding(start = MaterialTheme.dimens.small3),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}