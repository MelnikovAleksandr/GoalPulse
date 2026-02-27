package ru.asmelnikov.utils.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PagerTabRow(
    tabTitles: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onTabSelected: (Int) -> Unit
) {

    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = containerColor,
        tabs = {
            tabTitles.forEachIndexed { index, title ->
                val selected = selectedIndex == index

                Tab(
                    text = {
                        Text(
                            text = title,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1
                        )
                    },
                    selected = selected,
                    onClick = { onTabSelected(index) }
                )
            }
        }
    )
}