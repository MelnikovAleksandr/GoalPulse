package ru.asmelnikov.team_info.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingGif
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TeamInfoPage(
    teamInfo: TeamInfo,
    isLoading: Boolean,
    onReloadClick: () -> Unit,
    isMaterialColors: Boolean,
    stickyHeaderColor: Color,
    itemColor: Color,
    news: News,
    isLoadingNews: Boolean,
    onReloadNewsClick: () -> Unit
) {

    val brushColor: List<Color> = if (isMaterialColors)
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    else
        listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))

    val context = LocalContext.current

    when {
        isLoading && teamInfo.id.isBlank() -> LoadingGif()
        !isLoading && teamInfo.id.isBlank() -> EmptyContent(
            onReloadClick = onReloadClick
        )

        else -> {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = brushColor,
                            startY = 0f
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Area",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    SubComposeAsyncImageCommon(
                        imageUri = teamInfo.area.flag.ifBlank { R.drawable.unknown_flag },
                        shape = CircleShape,
                        size = MaterialTheme.dimens.medium2
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Address",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = teamInfo.address,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Founded",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = teamInfo.founded.toString(),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Venue",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = teamInfo.venue,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.small1)
                        .clickable {
                            Intent(Intent.ACTION_VIEW).also {
                                it.data = Uri.parse(teamInfo.website)
                                if (it.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(it)
                                }
                            }
                        },
                    text = teamInfo.website,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary
                )
                NewsList(news = news, isLoading = isLoadingNews, onReloadClick = onReloadNewsClick)
            }
        }
    }
}