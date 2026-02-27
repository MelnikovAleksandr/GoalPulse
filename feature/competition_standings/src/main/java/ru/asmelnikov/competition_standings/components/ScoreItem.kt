package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun ScoreItem(
    scoreHome: String,
    scoreAway: String
) {
    Card(
        modifier = Modifier.padding(horizontal = dimens.medium1),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Row(
            modifier = Modifier.padding(dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$scoreHome - $scoreAway",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview
@Composable
private fun ScorePreview1() {
    GoalPulseTheme(darkTheme = true) {
        Column (modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            Spacer(modifier = Modifier.height(6.dp))
            ScoreItem(
                scoreHome = "2", scoreAway = "1"
            )
            Spacer(modifier = Modifier.height(6.dp))
            ScoreItem(
                scoreHome = "0", scoreAway = "0"
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Preview
@Composable
private fun ScorePreview2() {
    GoalPulseTheme {
        Column (modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            Spacer(modifier = Modifier.height(6.dp))
            ScoreItem(
                scoreHome = "2", scoreAway = "1"
            )
            Spacer(modifier = Modifier.height(6.dp))
            ScoreItem(
                scoreHome = "0", scoreAway = "0"
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}