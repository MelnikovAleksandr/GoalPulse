package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.kyant.backdrop.Backdrop
import ru.asmelnikov.utils.composables.liquid.drawProgressivePlainBackdrop
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CompetitionsScaffoldTopBar(
    backdrop: Backdrop,
    competitionsCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchBarScrollState: SearchBarScrollState,
    modifier: Modifier = Modifier,
) {
    val blurRadiusPx = with(LocalDensity.current) { dimens.medium2.toPx() }
    val tint = MaterialTheme.colorScheme.background
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawProgressivePlainBackdrop(
                    backdrop = backdrop,
                    blurRadiusPx = blurRadiusPx,
                    tint = tint,
                    tintIntensity = 0.9f,
                ),
        )

        CompetitionsHeader(
            backdrop = backdrop,
            competitionsCount = competitionsCount,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            searchBarScrollState = searchBarScrollState,
        )
    }
}
