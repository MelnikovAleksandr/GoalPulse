package ru.asmelnikov.competitions_main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.getMockCompetitionsList
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.toLocalizedUiDate
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SharedTransitionScope.CompetitionItem(
    modifier: Modifier = Modifier,
    competition: Competition,
    backdrop: LayerBackdrop,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCompClick: (String, String) -> Unit,
) {
    LiquidCompetitionCard(
        backdrop = backdrop,
        modifier = modifier,
        onClick = { onCompClick(competition.id.toString(), competition.emblem) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.medium1),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SubComposeAsyncImageCommon(
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = competition.emblem),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    },
                ),
                imageUri = competition.emblem,
                shape = RoundedCornerShape(0.dp),
            )

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(1f).basicMarquee(Int.MAX_VALUE),
                        text = competition.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.width(dimens.small1))

                    SubComposeAsyncImageCommon(
                        imageUri = competition.area.flag.ifBlank { R.drawable.united_nations },
                        shape = CircleShape,
                        size = dimens.medium2,
                    )
                }

                Text(
                    text = "${stringResource(
                        R.string.current_match_day,
                    )} - ${competition.currentSeason.currentMatchDay}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.85f),
                )
                Text(
                    text = "${stringResource(
                        R.string.label_start_date,
                    )} - ${competition.currentSeason.startDate.toLocalizedUiDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.85f),
                )
                Text(
                    text = "${stringResource(
                        R.string.label_end_date,
                    )} - ${competition.currentSeason.endDate.toLocalizedUiDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun CompetitionItemPreview() {
    GoalPulseTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            AnimatedVisibility(visible = true) {
                val backdrop = rememberLayerBackdrop()
                CompetitionItem(
                    competition = getMockCompetitionsList().first().copy(name = "Very long name of some competition"),
                    backdrop = backdrop,
                    animatedVisibilityScope = this,
                    onCompClick = { _, _ -> },
                )
            }
        }
    }
}
