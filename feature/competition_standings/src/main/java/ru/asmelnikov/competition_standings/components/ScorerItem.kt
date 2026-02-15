package ru.asmelnikov.competition_standings.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun ScorerItem(
    modifier: Modifier = Modifier,
    scorer: Scorer,
    dataWeight: Float = 0.1f,
    index: Int,
    onPersonClick: (Int) -> Unit
) {

    val itemsRow = listOf(
        scorer.playedMatches.toString(),
        scorer.goals.toString(),
        scorer.assists.toString(),
        scorer.penalties.toString()
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium4)
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
                    strokeWidth = MaterialTheme.dimens.borderSize,
                    color = MaterialTheme.colorScheme.primary
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = index.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }


        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
                .rightBorder(
                    strokeWidth = MaterialTheme.dimens.borderSize,
                    color = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SubComposeAsyncImageCommon(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                    imageUri = scorer.team.crest,
                    shape = RoundedCornerShape(0.dp),
                    size = MaterialTheme.dimens.medium2
                )

                Column {
                    Text(
                        text = scorer.player.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                        strokeWidth = MaterialTheme.dimens.borderSize,
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private val topItemsRow = listOf("№", "Name", "M", "G", "A", "P")

@Composable
fun ScorerItemEmpty(
    modifier: Modifier = Modifier,
    dataWeight: Float = 0.1f
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium4)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {

        topItemsRow.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f)
                            .rightBorder(
                                strokeWidth = MaterialTheme.dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row {
                            Text(
                                text = item,
                                modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelLarge
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
                                strokeWidth = MaterialTheme.dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = item,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
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
        text = topItemsRow.drop(2).mapIndexed { index, route ->
            val secondRoute: String = when (index) {
                0 -> " - matches, "
                1 -> " - goals, "
                2 -> " - assists, "
                3 -> " - penalties."
                else -> ""
            }
            "$route $secondRoute"
        }.joinToString(" "),
        modifier = Modifier.padding(MaterialTheme.dimens.small1),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary
    )
}