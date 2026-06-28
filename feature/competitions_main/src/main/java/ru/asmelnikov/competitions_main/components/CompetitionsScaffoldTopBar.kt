package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.90f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.80f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.60f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.40f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.20f),
                        Color.Transparent
                    )
                )
            )
    ) {
        CompetitionsHeader(
            backdrop = backdrop,
            competitionsCount = competitionsCount,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )
    }
}
