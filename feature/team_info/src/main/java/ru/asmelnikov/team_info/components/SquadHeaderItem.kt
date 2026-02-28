package ru.asmelnikov.team_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.SquadByPosition
import ru.asmelnikov.utils.ui.theme.dimens
import ru.asmelnikov.utils.R

@Composable
fun SquadHeaderItem(
    squadByPosition: SquadByPosition,
    itemColor: Color,
    isMaterialColors: Boolean
) {

    Column(
        modifier = Modifier
            .background(if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else itemColor)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = dimens.small1,
                vertical = dimens.extraSmall1
            ),
            text = stringResource(squadByPosition.position.stringResId),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = dimens.small1,
                        vertical = dimens.extraSmall1
                    )
                    .weight(1f),
                text = stringResource(R.string.player_name),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = dimens.small1,
                        vertical = dimens.extraSmall1
                    )
                    .weight(1f),
                text = stringResource(R.string.player_nationality),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = dimens.small1,
                        vertical = dimens.extraSmall1
                    )
                    .weight(0.5f),
                text = stringResource(R.string.player_age),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}