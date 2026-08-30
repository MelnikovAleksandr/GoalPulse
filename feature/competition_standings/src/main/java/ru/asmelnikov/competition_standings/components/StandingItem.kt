package ru.asmelnikov.competition_standings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun StandingItem(
    modifier: Modifier = Modifier,
    table: Table,
    dataWeight: Float = 0.08f,
    firstBoxColor: Color = Color.Transparent,
    onTeamClick: (Int) -> Unit,
) {
    val itemsRow = remember(table) {
        listOf(
            table.position.toString(),
            "",
            table.playedGames.toString(),
            table.won.toString(),
            table.draw.toString(),
            table.lost.toString(),
            table.goalsFor.toString(),
            table.goalsAgainst.toString(),
            table.points.toString(),
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.medium4)
            .clickable(enabled = table.team.name.isNotEmpty()) {
                onTeamClick(table.team.id)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsRow.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.37f)
                            .rightBorder(
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Row {
                            SubComposeAsyncImageCommon(
                                modifier = Modifier.padding(horizontal = dimens.small1),
                                imageUri = table.team.crest,
                                shape = RoundedCornerShape(0.dp),
                                size = dimens.medium2,
                            )

                            Text(
                                text = table.team.shortName,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = item,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StandingTopItem(
    modifier: Modifier = Modifier,
    tableName: String,
    dataWeight: Float = 0.08f,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .height(dimens.medium4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TableColumn.entries.forEachIndexed { index, item ->
            when (index) {
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.37f)
                            .rightBorder(
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = dimens.small1),
                            text = tableName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(dataWeight)
                            .rightBorder(
                                strokeWidth = dimens.borderSize,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(item.titleResId),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
        text = buildString {
            TableColumn.entries.drop(2).forEach { column ->
                append("${stringResource(column.titleResId)} ")
                append("${stringResource(column.descriptionResId)} ")
            }
        },
        modifier = Modifier.padding(dimens.small1).navigationBarsPadding(),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary,
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
                        strokeWidth = strokeWidthPx,
                    )
                }
            },
        )
    }

fun Modifier.leftRoundedBorder(
    strokeWidth: Dp = 6.dp,
    color: Color,
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
                    size = Size(strokeWidthPx, height),
                )

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(strokeWidthPx, 0f),
                    size = Size(width - strokeWidthPx, height),
                )
            }
        },
    )
}

enum class TableColumn(@StringRes val titleResId: Int, @StringRes val descriptionResId: Int) {
    POSITION(
        titleResId = R.string.table_column_position,
        descriptionResId = R.string.table_description_position,
    ),
    TEAM(
        titleResId = R.string.table_column_team,
        descriptionResId = R.string.table_description_team,
    ),
    MATCHES(
        titleResId = R.string.table_column_matches,
        descriptionResId = R.string.table_description_matches,
    ),
    WINS(
        titleResId = R.string.table_column_wins,
        descriptionResId = R.string.table_description_wins,
    ),
    DRAWS(
        titleResId = R.string.table_column_draws,
        descriptionResId = R.string.table_description_draws,
    ),
    LOSSES(
        titleResId = R.string.table_column_losses,
        descriptionResId = R.string.table_description_losses,
    ),
    GOALS_FOR(
        titleResId = R.string.table_column_goals_for,
        descriptionResId = R.string.table_description_goals_for,
    ),
    GOALS_AGAINST(
        titleResId = R.string.table_column_goals_against,
        descriptionResId = R.string.table_description_goals_against,
    ),
    POINTS(
        titleResId = R.string.table_column_points,
        descriptionResId = R.string.table_description_points,
    ),
}
