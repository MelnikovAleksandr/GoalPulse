package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.getMockTeam
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.composables.liquid.LiquidPullToRefreshWrapper
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TeamInfoPage(
    teamInfo: TeamInfo,
    isLoading: Boolean,
    onReloadClick: () -> Unit,
    isPullToRefreshEnabled: Boolean,
    topInset: Dp,
    news: News,
    isLoadingNews: Boolean,
    onPullActiveChange: (Boolean) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    val context = LocalContext.current
    AnimatedContent(
        targetState = teamInfo.name.isEmpty(),
        modifier = Modifier.fillMaxSize(),
    ) { emptyState ->
        if (emptyState && !isLoading) {
            EmptyContent(
                modifier = Modifier.padding(top = topInset),
                onReloadClick = onReloadClick,
            )
        } else {
            LiquidPullToRefreshWrapper(
                modifier = Modifier.fillMaxSize(),
                backdrop = backdrop,
                isRefreshing = isLoading,
                onRefresh = onReloadClick,
                enabled = isPullToRefreshEnabled,
                onPullActiveChange = onPullActiveChange,
                topOffset = topInset,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(dimens.small1),
                    contentPadding = PaddingValues(
                        bottom = dimens.small1,
                        start = dimens.small1,
                        end = dimens.small1,
                        top = topInset,
                    ),
                ) {
                    item {
                        Column(
                            modifier = Modifier,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.small1),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = stringResource(R.string.team_info_area),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                SubComposeAsyncImageCommon(
                                    imageUri = teamInfo.area.flag.ifBlank { R.drawable.unknown_flag },
                                    shape = CircleShape,
                                    size = dimens.medium2,
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.small1),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = stringResource(R.string.team_info_address),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = teamInfo.address,
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.small1),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = stringResource(R.string.team_info_founded),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = teamInfo.founded.toString(),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.small1),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = stringResource(R.string.team_info_venue),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = teamInfo.venue,
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            TextButton(
                                modifier = Modifier.padding(dimens.small1),
                                onClick = {
                                    getWebIntent(teamInfo.website, context)
                                },
                            ) {
                                Text(
                                    text = teamInfo.website,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = stringResource(R.string.team_news),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    items(
                        items = news.articles,
                    ) { article ->
                        ArticleItem(
                            modifier = Modifier.animateItem(),
                            article = article,
                            onArticleClick = { url ->
                                getWebIntent(url, context)
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
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
            isPullToRefreshEnabled = true,
            topInset = Dp.Hairline,
            news = News(),
            isLoadingNews = false,
        )
    }
}
