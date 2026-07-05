package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.getMockScorers
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SecondPagerScreenScorers(
    scorers: List<Scorer>,
    paddingValues: PaddingValues = PaddingValues(),
    isLoadingScorers: Boolean,
    onReloadClick: () -> Unit,
    onPersonClick: (Int) -> Unit
) {
    val topInset = paddingValues.calculateTopPadding()
    Column(modifier = Modifier.fillMaxSize()) {

        AnimatedContent(targetState = scorers.isEmpty()) { emptyData ->
            when {
                isLoadingScorers && emptyData -> LoadingBall(modifier = Modifier.padding(top = topInset))
                !isLoadingScorers && emptyData -> EmptyContent(
                    modifier = Modifier.padding(top = topInset),
                    onReloadClick = onReloadClick
                )

                else -> {
                    val listState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
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
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                AnimatedVisibility(visible = isLoadingScorers) {
                                    LinearProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(dimens.extraSmall1),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.background
                                    )
                                }
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
                isLoadingScorers = false,
                onReloadClick = {},
                onPersonClick = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun ScorersPreview2() {
    GoalPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SecondPagerScreenScorers(
                scorers = getMockScorers(),
                isLoadingScorers = false,
                onReloadClick = {},
                onPersonClick = {}
            )
        }
    }
}