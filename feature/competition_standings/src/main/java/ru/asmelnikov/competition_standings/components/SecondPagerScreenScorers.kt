package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.getMockScorers
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.liquid.LiquidPullToRefreshWrapper
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@Composable
fun SecondPagerScreenScorers(
    scorers: List<Scorer>,
    topInset: Dp,
    isPullToRefreshEnabled: Boolean,
    isLoadingScorers: Boolean,
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
        targetState = scorers.isEmpty(),
        modifier = Modifier.fillMaxSize()
    ) { emptyState ->
        if (emptyState && !isLoadingScorers) {
            EmptyContent(
                modifier = Modifier.padding(top = topInset),
                onReloadClick = onReloadClick
            )
        } else {
            LiquidPullToRefreshWrapper(
                modifier = Modifier.fillMaxSize(),
                backdrop = backdrop,
                isRefreshing = isLoadingScorers,
                onRefresh = onReloadClick,
                enabled = isPullToRefreshEnabled,
                onPullActiveChange = onPullActiveChange,
                topOffset = topInset
            ) {
                LazyColumn(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .fillMaxSize()
                        .background(backgroundColor),
                    state = listState,
                    contentPadding = PaddingValues(top = topInset)
                ) {
                    stickyHeaderContentPaddingAware(
                        listState = listState,
                        key = "stickyKey",
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor)
                        ) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                            ScorerItemEmpty()
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    itemsIndexed(
                        items = scorers,
                        key = { _, scorer -> scorer.player.id }) { index, scorer ->
                        ScorerItem(
                            modifier = Modifier.animateItem(),
                            scorer = scorer,
                            index = index + 1,
                            onPersonClick = onPersonClick
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                    }
                    item {
                        BottomScorerItem()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun ScorersPreview1() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SecondPagerScreenScorers(
                scorers = getMockScorers(),
                isLoadingScorers = true,
                isPullToRefreshEnabled = true,
                topInset = Dp.Hairline,
                onReloadClick = {},
                onPersonClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun ScorersPreview2() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SecondPagerScreenScorers(
                scorers = emptyList(),
                isLoadingScorers = false,
                isPullToRefreshEnabled = true,
                topInset = Dp.Hairline,
                onReloadClick = {},
                onPersonClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun ScorersPreview3() {
    GoalPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SecondPagerScreenScorers(
                scorers = getMockScorers(),
                isLoadingScorers = true,
                isPullToRefreshEnabled = true,
                topInset = Dp.Hairline,
                onReloadClick = {},
                onPersonClick = {}
            )
        }
    }
}
