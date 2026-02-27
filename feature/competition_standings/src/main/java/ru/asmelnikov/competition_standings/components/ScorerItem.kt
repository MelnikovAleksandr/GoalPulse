package ru.asmelnikov.competition_standings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.R

@Composable
fun ScorerItem(
    modifier: Modifier = Modifier,
    scorer: Scorer,
    dataWeight: Float = 0.1f,
    index: Int,
    onPersonClick: (Int) -> Unit
) {

    val itemsRow = remember {
        listOf(
            scorer.playedMatches.toString(),
            scorer.goals.toString(),
            scorer.assists.toString(),
            scorer.penalties.toString()
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.medium4)
            .clickable {
                onPersonClick(scorer.player.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(
                    strokeWidth = dimens.borderSize,
                    color = MaterialTheme.colorScheme.primary
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = index.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }


        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
                .rightBorder(
                    strokeWidth = dimens.borderSize,
                    color = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SubComposeAsyncImageCommon(
                    modifier = Modifier.padding(horizontal = dimens.small1),
                    imageUri = scorer.team.crest,
                    shape = RoundedCornerShape(0.dp),
                    size = dimens.medium2
                )

                Column {
                    Text(
                        text = scorer.player.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = scorer.team.shortName,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        itemsRow.forEach {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(dataWeight)
                    .rightBorder(
                        strokeWidth = dimens.borderSize,
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun ScorerItemEmpty(
    modifier: Modifier = Modifier,
    dataWeight: Float = 0.1f
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .height(dimens.medium4),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TopPlayersColumn.entries.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f)
                            .rightBorder(
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row {
                            Text(
                                text = stringResource(item.titleResId),
                                modifier = Modifier.padding(start = dimens.small1),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(dataWeight)
                            .rightBorder(
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(item.titleResId),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomScorerItem() {
    Text(
        text = buildString {
            TopPlayersColumn.entries.drop(2).forEach { column ->
                append("${stringResource(column.titleResId)} ")
                append("${stringResource(column.descriptionResId)} ")
            }
        },
        modifier = Modifier.padding(dimens.small1),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary
    )
}

enum class TopPlayersColumn(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int
) {
    POSITION(
        titleResId = R.string.top_players_column_position,
        descriptionResId = R.string.top_players_description_position
    ),
    NAME(
        titleResId = R.string.top_players_column_name,
        descriptionResId = R.string.top_players_description_name
    ),
    MATCHES(
        titleResId = R.string.top_players_column_matches,
        descriptionResId = R.string.top_players_description_matches
    ),
    GOALS(
        titleResId = R.string.top_players_column_goals,
        descriptionResId = R.string.top_players_description_goals
    ),
    ASSISTS(
        titleResId = R.string.top_players_column_assists,
        descriptionResId = R.string.top_players_description_assists
    ),
    PENALTIES(
        titleResId = R.string.top_players_column_penalties,
        descriptionResId = R.string.top_players_description_penalties
    )
}