package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.ui.theme.topGreen
import ru.asmelnikov.utils.R

@Composable
fun Head2headView(head2head: Head2head) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.medium3)
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
                        text = "${stringResource(R.string.stat_home_wins_hyphen)} ${head2head.aggregates.homeWinsPercentage.toInt()}% (${head2head.aggregates.homeTeam.wins})",
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
                        text = "${stringResource(R.string.stat_draws_hyphen)} ${head2head.aggregates.drawsPercentage.toInt()}% (${head2head.aggregates.homeTeam.draws})",
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
                        text = "${stringResource(R.string.stat_away_wins_hyphen)} ${head2head.aggregates.awayWinsPercentage.toInt()}% (${head2head.aggregates.homeTeam.losses})",
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