package ru.asmelnikov.utils.composables

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
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.ui.theme.topGreen
import ru.asmelnikov.utils.R

@Composable
fun Head2headView(
    homeWinsPercentage: Float,
    drawsPercentage: Float,
    awayWinsPercentage: Float,
    homeWins: Int,
    homeDraws: Int,
    homeLosses: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.medium3)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            if (homeWinsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(homeWinsPercentage)
                        .background(topGreen)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "${stringResource(R.string.stat_home_wins_hyphen)} ${homeWinsPercentage.toInt()}% (${homeWins})",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            if (drawsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(drawsPercentage)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "${stringResource(R.string.stat_draws_hyphen)} ${drawsPercentage.toInt()}% (${homeDraws})",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            if (awayWinsPercentage > 0)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(awayWinsPercentage)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = "${stringResource(R.string.stat_away_wins_hyphen)} ${awayWinsPercentage.toInt()}% (${homeLosses})",
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