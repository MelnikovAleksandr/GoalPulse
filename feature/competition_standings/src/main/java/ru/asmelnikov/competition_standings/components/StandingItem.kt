package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun StandingItem(
    modifier: Modifier = Modifier,
    table: Table,
    dataWeight: Float = 0.08f,
    firstBoxColor: Color = Color.Transparent,
    onTeamClick: (Int) -> Unit
) {

    val itemsRow = listOf(
        table.position.toString(),
        "",
        table.playedGames.toString(),
        table.won.toString(),
        table.draw.toString(),
        table.lost.toString(),
        table.goalsFor.toString(),
        table.goalsAgainst.toString(),
        table.points.toString()
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium4)
            .clickable {
                onTeamClick(table.team.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsRow.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.37f)
                            .rightBorder(
                                strokeWidth = MaterialTheme.dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row {
                            SubComposeAsyncImageCommon(
                                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                                imageUri = table.team.crest,
                                shape = RoundedCornerShape(0.dp),
                                size = MaterialTheme.dimens.medium2
                            )

                            Text(
                                text = table.team.shortName,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(dataWeight)
                            .leftRoundedBorder(color = if (index == 0) firstBoxColor else Color.Transparent)
                            .rightBorder(
                                strokeWidth = MaterialTheme.dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = item,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

private val topListRow = listOf("№", "Team", "M", "W", "D", "L", "GF", "GA", "P")

@Composable
fun StandingTopItem(
    modifier: Modifier = Modifier,
    tableName: String = "Team",
    dataWeight: Float = 0.08f
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        topListRow.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.37f)
                            .rightBorder(
                                strokeWidth = MaterialTheme.dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = MaterialTheme.dimens.small1),
                            text = tableName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
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
fun BottomStandingItem() {
    Text(
        text = topListRow.drop(2).mapIndexed { index, route ->
            val secondRoute: String = when (index) {
                0 -> " - matches, "
                1 -> " - wins, "
                2 -> " - draws, "
                3 -> " - loses, "
                4 -> " - goals for, "
                5 -> " - goals against, "
                6 -> " - points."
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

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) =
    composed {
        composed(
            factory = {
                val density = LocalDensity.current
                val strokeWidthPx = density.run { strokeWidth.toPx() }

                Modifier.drawBehind {
                    val width = size.width - strokeWidthPx / 2
                    val height = size.height

                    drawLine(
                        color = color,
                        start = Offset(x = width, y = 0f),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }
            }
        )
    }


fun Modifier.leftRoundedBorder(
    strokeWidth: Dp = 6.dp,
    color: Color
) = composed {
    composed(
        factory = {
            val density = LocalDensity.current
            val strokeWidthPx = density.run { strokeWidth.toPx() }

            Modifier.drawBehind {
                val width = size.width - strokeWidthPx / 2
                val height = size.height

                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, 0f),
                    size = Size(strokeWidthPx, height)
                )

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(strokeWidthPx, 0f),
                    size = Size(width - strokeWidthPx, height)
                )
            }
        }
    )
}
