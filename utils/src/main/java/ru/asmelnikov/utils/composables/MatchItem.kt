package ru.asmelnikov.utils.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens
import java.util.UUID

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    matchId: Int,
    homeTeamCrest: String,
    homeName: String,
    awayTeamCrest: String,
    awayName: String,
    bigDate: String,
    fullDate: String,
    homeScore: String,
    awayScore: String,
    isAhead: Boolean,
    expandedItemId: Int,
    numberOfMatches: Int,
    homeWinsPercentage: Float,
    drawsPercentage: Float,
    awayWinsPercentage: Float,
    homeWins: Int,
    homeDraws: Int,
    homeLosses: Int,
    head2headId: Int,
    isHead2headLoading: Boolean,
    onMatchItemClick: (Int) -> Unit,
    compName: String = "",
    color: Color = MaterialTheme.colorScheme.primary,
    isInCalendar: Boolean = false,
    isCalendarLoading: Boolean = false,
    onCalendarClick: () -> Unit = {}
) {

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isHead2headLoading) {
                    onMatchItemClick(matchId)
                },
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
                    imageUri = homeTeamCrest,
                    shape = RoundedCornerShape(0.dp),
                    size = dimens.medium4
                )
                if (isAhead) {
                    Text(
                        modifier = Modifier.padding(horizontal = dimens.medium1),
                        text = bigDate,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ScoreItem(
                        scoreHome = homeScore,
                        scoreAway = awayScore,
                        color = color
                    )
                }
                SubComposeAsyncImageCommon(
                    modifier = Modifier.padding(horizontal = dimens.small1),
                    imageUri = awayTeamCrest,
                    shape = RoundedCornerShape(0.dp),
                    size = dimens.medium4
                )
            }
            Text(
                modifier = Modifier.padding(top = dimens.small3),
                text = "$homeName - $awayName",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier.padding(bottom = dimens.medium1),
                text = "$fullDate ${compName.ifEmpty { "" }}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary
            )
            AnimatedVisibility(visible = expandedItemId == matchId) {
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
                    visible = matchId == head2headId
                ) {
                    if (numberOfMatches < 1) {
                        Text(
                            text = stringResource(R.string.first_match_no_statistics),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Head2headView(
                            homeWinsPercentage = homeWinsPercentage,
                            drawsPercentage = drawsPercentage,
                            awayWinsPercentage = awayWinsPercentage,
                            homeWins = homeWins,
                            homeDraws = homeDraws,
                            homeLosses = homeLosses
                        )
                    }
                }
            }
        }
        if (isAhead) {
            MatchCalendarButton(
                modifier = Modifier.align(Alignment.TopEnd),
                isInCalendar = isInCalendar,
                isLoading = isCalendarLoading,
                onClick = onCalendarClick
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview1() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = UUID.randomUUID().hashCode(),
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview2() {
    GoalPulseTheme {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = false,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = UUID.randomUUID().hashCode(),
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview3() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = true,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = UUID.randomUUID().hashCode(),
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046,
            isInCalendar = true
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview4() {
    GoalPulseTheme {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = true,
            expandedItemId = -1,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = UUID.randomUUID().hashCode(),
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046,
            isCalendarLoading = false
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview5() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = false,
            expandedItemId = 538046,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = 538046,
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 0,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview6() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = false,
            expandedItemId = 538046,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = 538046,
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun MatchItemPreview7() {
    GoalPulseTheme(darkTheme = true) {
        MatchItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isAhead = true,
            expandedItemId = 538046,
            onMatchItemClick = {},
            isHead2headLoading = false,
            matchId = 538046,
            homeTeamCrest = "",
            homeName = "Sunderland",
            awayTeamCrest = "https://crests.football-data.org/66.png",
            awayName = "Man United",
            bigDate = "28.02",
            fullDate = "2026-05-09T14:00:00Z",
            homeScore = "1",
            awayScore = "2",
            numberOfMatches = 9,
            homeWinsPercentage = 35f,
            drawsPercentage = 20f,
            awayWinsPercentage = 45f,
            homeWins = 5,
            homeDraws = 3,
            homeLosses = 1,
            head2headId = 538046
        )
    }
}
