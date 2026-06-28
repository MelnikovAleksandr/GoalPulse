package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.kyant.backdrop.Backdrop

@Composable
fun CompetitionsScaffoldTopBar(
    backdrop: Backdrop,
    competitionsCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchBarScrollState: SearchBarScrollState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val scrimGradient = remember(backgroundColor) {
        Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = 0.90f),
                backgroundColor.copy(alpha = 0.90f),
                backgroundColor.copy(alpha = 0.80f),
                backgroundColor.copy(alpha = 0.60f),
                backgroundColor.copy(alpha = 0.40f),
                Color.Transparent
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = scrimGradient)
    ) {
        CompetitionsHeader(
            backdrop = backdrop,
            competitionsCount = competitionsCount,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            searchBarScrollState = searchBarScrollState
        )
    }
}
