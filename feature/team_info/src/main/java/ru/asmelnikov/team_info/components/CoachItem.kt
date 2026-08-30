package ru.asmelnikov.team_info.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.Coach
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CoachItem(
    coach: Coach,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(dimens.small3),
            text = stringResource(R.string.team_manager),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier.padding(dimens.small3),
            text = coach.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall.copy(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
