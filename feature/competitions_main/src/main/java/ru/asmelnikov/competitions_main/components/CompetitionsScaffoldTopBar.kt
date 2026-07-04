package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.effects.blur
import ru.asmelnikov.utils.ui.theme.dimens

private const val BLUR_RAMP_START_FRACTION = 0.05f

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
    val blurRadiusPx = with(LocalDensity.current) { dimens.medium5.toPx() }

    val progressiveBlurMask = remember {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0f to backgroundColor,
                1f - BLUR_RAMP_START_FRACTION to Color.Transparent,
                1f to Color.Transparent,
            )
        )
    }

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

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawPlainBackdrop(
                    backdrop = backdrop,
                    shape = { RectangleShape },
                    effects = { blur(blurRadiusPx) },
                )
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = progressiveBlurMask,
                        blendMode = BlendMode.DstIn,
                    )
                }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = scrimGradient)
        )

        CompetitionsHeader(
            backdrop = backdrop,
            competitionsCount = competitionsCount,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            searchBarScrollState = searchBarScrollState
        )
    }
}
