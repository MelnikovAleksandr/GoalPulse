package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.getMockTeam
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
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
    isLoadingNews: Boolean
) {

    val colorScheme = MaterialTheme.colorScheme

    val brushColor = remember(isMaterialColors, stickyHeaderColor, itemColor) {
        if (isMaterialColors)
            listOf(colorScheme.background, colorScheme.background)
        else
            listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))
    }

    val context = LocalContext.current

    AnimatedContent(targetState = teamInfo.name.isEmpty()) { emptyData ->
        when {
            isLoading && emptyData -> LoadingBall()
            !isLoading && emptyData -> EmptyContent(
                onReloadClick = onReloadClick
            )

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                            .padding(dimens.small1),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.team_info_area),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        SubComposeAsyncImageCommon(
                            imageUri = teamInfo.area.flag.ifBlank { R.drawable.unknown_flag },
                            shape = CircleShape,
                            size = dimens.medium2
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.small1),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.team_info_address),
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
                            .padding(dimens.small1),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.team_info_founded),
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
                            .padding(dimens.small1),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.team_info_venue),
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

                    TextButton(
                        modifier = Modifier.padding(dimens.small1),
                        onClick = {
                            getWebIntent(teamInfo.website, context)
                        }
                    ) {
                        Text(
                            text = teamInfo.website,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    NewsList(news = news, isLoading = isLoadingNews)
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TeamInfoPagePreview() {
    GoalPulseTheme(darkTheme = true) {
        TeamInfoPage(
            teamInfo = getMockTeam(),
            isLoading = false,
            onReloadClick = {},
            isMaterialColors = true,
            stickyHeaderColor = Color.White,
            itemColor = Color.White,
            news = News(),
            isLoadingNews = false
        )
    }
}