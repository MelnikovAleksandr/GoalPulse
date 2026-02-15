package ru.asmelnikov.team_info.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.Coach
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CoachItem(
    coach: Coach
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { }) {
        Text(
            modifier = Modifier.padding(MaterialTheme.dimens.small3),
            text = "Manager",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Divider(color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier.padding(MaterialTheme.dimens.small3),
            text = coach.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}