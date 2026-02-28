package ru.asmelnikov.team_info.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.getMockTeam
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@Composable
fun SquadPagerList(
    teamInfo: TeamInfo,
    stickyHeaderColor: Color,
    itemColor: Color,
    isMaterialColors: Boolean,
    isLoading: Boolean,
    onReloadClick: () -> Unit,
    onPersonClick: (Int) -> Unit
) {

    val scheme = MaterialTheme.colorScheme

    val brushColor = remember(isMaterialColors, stickyHeaderColor, itemColor) {
        if (isMaterialColors)
            listOf(scheme.background, scheme.background)
        else
            listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))
    }

    AnimatedContent(targetState = teamInfo.squadByPosition.isEmpty()) { emptyData ->
        when {
            isLoading && emptyData -> LoadingBall()
            !isLoading && emptyData -> EmptyContent(
                onReloadClick = onReloadClick
            )

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = brushColor,
                                startY = 0f
                            )
                        )
                ) {
                    item {
                        CoachItem(coach = teamInfo.coach)
                    }
                    teamInfo.squadByPosition.forEach { squadByPosition ->
                        stickyHeader {
                            SquadHeaderItem(
                                squadByPosition = squadByPosition,
                                itemColor = itemColor,
                                isMaterialColors = isMaterialColors
                            )
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
            stickyHeaderColor = Color.White,
            itemColor = Color.White,
            isMaterialColors = true,
            isLoading = false,
            onReloadClick = {},
            onPersonClick = {}
        )
    }
}