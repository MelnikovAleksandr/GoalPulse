package ru.asmelnikov.team_info.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.Squad
import ru.asmelnikov.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SquadItem(
    squad: Squad,
    onPersonClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable {
                onPersonClick(squad.id)
            }
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.dimens.small1)
                .basicMarquee(iterations = Int.MAX_VALUE),
            text = squad.name,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.dimens.small1),
            text = squad.nationality,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .weight(0.5f)
                .padding(MaterialTheme.dimens.small1),
            text = "${squad.age} y.o.",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}