package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.getMockTeam
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.liquid.LiquidPullToRefreshWrapper
import ru.asmelnikov.utils.composables.stickyHeaderContentPaddingAware
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@Composable
fun SquadPagerList(
    teamInfo: TeamInfo,
    topInset: Dp,
    itemColor: Color,
    isMaterialColors: Boolean,
    isLoading: Boolean,
    isPullToRefreshEnabled: Boolean,
    onReloadClick: () -> Unit,
    onPersonClick: (Int) -> Unit,
    onPullActiveChange: (Boolean) -> Unit = {}
) {

    val listState = rememberLazyListState()
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

    AnimatedContent(
        targetState = teamInfo.squadByPosition.isEmpty(),
        modifier = Modifier.fillMaxSize()
    ) { emptyData ->
        if (emptyData && !isLoading) {
            EmptyContent(
                modifier = Modifier.padding(top = topInset),
                onReloadClick = onReloadClick
            )
        } else {
            LiquidPullToRefreshWrapper(
                modifier = Modifier.fillMaxSize(),
                backdrop = backdrop,
                isRefreshing = isLoading,
                onRefresh = onReloadClick,
                enabled = isPullToRefreshEnabled,
                onPullActiveChange = onPullActiveChange,
                topOffset = topInset
            ) {
                LazyColumn(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(top = topInset)
                ) {
                    item {
                        CoachItem(coach = teamInfo.coach)
                    }
                    teamInfo.squadByPosition.forEachIndexed { index, squadByPosition ->
                        val stickyKey = "team_sticky_$index"
                        stickyHeaderContentPaddingAware(
                            listState = listState,
                            key = stickyKey,
                            contentType = stickyKey
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(backgroundColor)
                            ) {
                                SquadHeaderItem(
                                    squadByPosition = squadByPosition,
                                    itemColor = itemColor,
                                    isMaterialColors = isMaterialColors
                                )
                            }
                        }
                        itemsIndexed(
                            items = squadByPosition.squad,
                            key = { _, squadItem -> squadItem.id }) { index, squadItem ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.Transparent
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SquadItem(squad = squadItem, onPersonClick = onPersonClick)
                                if (index < squadByPosition.squad.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
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
private fun SquadPagerListPreview() {
    GoalPulseTheme(darkTheme = true) {
        SquadPagerList(
            teamInfo = getMockTeam(),
            topInset = Dp.Hairline,
            isLoading = false,
            itemColor = MaterialTheme.colorScheme.primaryContainer,
            isMaterialColors = true,
            isPullToRefreshEnabled = true,
            onReloadClick = {},
            onPersonClick = {}
        )
    }
}