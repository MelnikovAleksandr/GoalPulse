package ru.asmelnikov.competition_standings.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.getMockHead2Head
import ru.asmelnikov.domain.models.getMockMatches
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match: Match,
    isAhead: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
//            .clickable(enabled = !isHead2headLoading) { TODO api Head2head wrong data, replace after v5
//                onMatchItemClick(match.id)
//            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimens.medium1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = dimens.small1),
                imageUri = match.homeTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = dimens.medium4
            )
            if (isAhead) {
                Text(
                    modifier = Modifier.padding(horizontal = dimens.medium1),
                    text = match.bigDate,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                ScoreItem(
                    match.score.fullTime.home.toString(),
                    match.score.fullTime.away.toString()
                )
            }
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = dimens.small1),
                imageUri = match.awayTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = dimens.medium4
            )
        }
        Text(
            modifier = Modifier.padding(top = dimens.small3),
            text = "${match.homeTeam.shortName} - ${match.awayTeam.shortName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            modifier = Modifier.padding(bottom = dimens.medium1),
            text = match.utcDate,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.secondary
        )
        AnimatedVisibility(visible = expandedItemId == match.id) {
            AnimatedVisibility(
                visible = isHead2headLoading
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.extraSmall1),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            AnimatedVisibility(
                visible = match.id == head2head.id
            ) {
                if (head2head.aggregates.numberOfMatches < 1) {
                    Text(
                        text = stringResource(R.string.first_match_no_statistics),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Head2headView(head2head)
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview1() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourCompleted.first().matches.first(),
            isAhead = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = Head2head(),
            isHead2headLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview2() {
    GoalPulseTheme {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourCompleted.first().matches.first(),
            isAhead = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = Head2head(),
            isHead2headLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview3() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourAhead.first().matches.first(),
            isAhead = true,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = Head2head(),
            isHead2headLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview4() {
    GoalPulseTheme {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourAhead.first().matches.first(),
            isAhead = true,
            expandedItemId = -1,
            onMatchItemClick = {},
            head2head = Head2head(),
            isHead2headLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview5() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourCompleted.first().matches.first().copy(id = 538046),
            isAhead = false,
            expandedItemId = 538046,
            onMatchItemClick = {},
            head2head = Head2head(
                id = 538046
            ),
            isHead2headLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview6() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            match = getMockMatches().matchesByTourCompleted.first().matches.first().copy(id = 538046),
            isAhead = false,
            expandedItemId = 538046,
            onMatchItemClick = {},
            head2head = getMockHead2Head(),
            isHead2headLoading = false
        )
    }
}
