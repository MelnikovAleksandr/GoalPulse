package ru.asmelnikov.team_info.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingGif


@OptIn(ExperimentalFoundationApi::class)
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

    val brushColor: List<Color> = if (isMaterialColors)
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    else
        listOf(stickyHeaderColor.copy(alpha = 0.5f), itemColor.copy(alpha = 0.5f))

    when {
        isLoading && teamInfo.squadByPosition.isEmpty() -> LoadingGif()
        !isLoading && teamInfo.squadByPosition.isEmpty() -> EmptyContent(
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
                                Divider(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
