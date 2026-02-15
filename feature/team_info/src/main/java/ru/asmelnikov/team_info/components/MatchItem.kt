package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.ui.theme.lastRed
import ru.asmelnikov.utils.ui.theme.topGreen

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match: Match,
    isAhead: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head,
    isHead2headLoading: Boolean,
    teamId: String
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isHead2headLoading) {
                onMatchItemClick(match.id)
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.medium1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                imageUri = match.homeTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = MaterialTheme.dimens.medium4
            )
            if (isAhead) {
                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
                    text = match.bigDate,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                ScoreItem(teamId, match)
            }
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                imageUri = match.awayTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = MaterialTheme.dimens.medium4
            )
        }
        Text(
            modifier = modifier.padding(top = MaterialTheme.dimens.small3),
            text = "${match.homeTeam.shortName} - ${match.awayTeam.shortName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = modifier.padding(bottom = MaterialTheme.dimens.medium1),
            text = "${match.utcDate} - ${match.area.name}. ${match.competition.name}",
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
                        .height(MaterialTheme.dimens.extraSmall1),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            AnimatedVisibility(
                visible = match.id == head2head.id
            ) {
                if (head2head.aggregates.numberOfMatches < 1) {
                    Text(
                        text = "There are no statistics, this is the first match.",
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

@Composable
fun ScoreItem(
    teamId: String,
    match: Match
) {

    val isHomeTeam = teamId == match.homeTeam.id.toString()
    val isAwayTeam = teamId == match.awayTeam.id.toString()

    val resultColor = when (match.score.winner) {
        "HOME_TEAM" -> if (isHomeTeam) MaterialTheme.colorScheme.primary else lastRed.copy(alpha = 0.5f)
        "AWAY_TEAM" -> if (isAwayTeam) MaterialTheme.colorScheme.primary else lastRed.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
        colors = CardDefaults.cardColors(
            containerColor = resultColor
        ),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${match.score.fullTime.home} - ${match.score.fullTime.away}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Head2headView(head2head: Head2head) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium3)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            if (head2head.aggregates.homeWinsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(head2head.aggregates.homeWinsPercentage)
                        .background(topGreen)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "Home wins - ${head2head.aggregates.homeWinsPercentage.toInt()}% (${head2head.aggregates.homeTeam.wins})",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            if (head2head.aggregates.drawsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(head2head.aggregates.drawsPercentage)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "Draws - ${head2head.aggregates.drawsPercentage.toInt()}% (${head2head.aggregates.homeTeam.draws})",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            if (head2head.aggregates.awayWinsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(head2head.aggregates.awayWinsPercentage)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "Away wins - ${head2head.aggregates.awayWinsPercentage.toInt()}% (${head2head.aggregates.homeTeam.losses})",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
        }
    }
}
