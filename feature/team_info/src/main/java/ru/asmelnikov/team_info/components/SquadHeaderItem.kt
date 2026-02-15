package ru.asmelnikov.team_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.SquadByPosition
import ru.asmelnikov.utils.ui.theme.dimens

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
                horizontal = MaterialTheme.dimens.small1,
                vertical = MaterialTheme.dimens.extraSmall1
            ),
            text = squadByPosition.position,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimens.small1,
                        vertical = MaterialTheme.dimens.extraSmall1
                    )
                    .weight(1f),
                text = "Name",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimens.small1,
                        vertical = MaterialTheme.dimens.extraSmall1
                    )
                    .weight(1f),
                text = "Nationality",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimens.small1,
                        vertical = MaterialTheme.dimens.extraSmall1
                    )
                    .weight(0.5f),
                text = "Age",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}