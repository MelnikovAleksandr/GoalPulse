package ru.asmelnikov.utils.composables.liquid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import ru.asmelnikov.utils.ui.theme.Dimens
import ru.asmelnikov.utils.ui.theme.dimens

object LiquidGlassDefaults {
    const val COMPACT_SURFACE_ALPHA = 0.10f
    const val COMPACT_OVERLAY_ALPHA = 0.05f
    const val CARD_SURFACE_ALPHA = 0.12f
    const val CARD_OVERLAY_ALPHA = 0.06f
}

data class LiquidGlassEffectSpec(
    val blurPx: Float,
    val lensWidthPx: Float,
    val lensHeightPx: Float
)

@Composable
fun rememberLiquidGlassCompactSpec(): LiquidGlassEffectSpec {
    val density = LocalDensity.current
    val appDimens = dimens
    return remember(
        appDimens.small1,
        appDimens.small3,
        appDimens.medium2,
        density
    ) {
        appDimens.liquidGlassCompactSpec(density)
    }
}

@Composable
fun rememberLiquidGlassCardSpec(): LiquidGlassEffectSpec {
    val density = LocalDensity.current
    val appDimens = dimens
    return remember(
        appDimens.small1,
        appDimens.medium1,
        appDimens.medium3,
        density
    ) {
        appDimens.liquidGlassCardSpec(density)
    }
}

fun Dimens.liquidGlassCompactSpec(density: Density): LiquidGlassEffectSpec = with(density) {
    LiquidGlassEffectSpec(
        blurPx = small1.toPx(),
        lensWidthPx = small3.toPx(),
        lensHeightPx = medium2.toPx()
    )
}

fun Dimens.liquidGlassCardSpec(density: Density): LiquidGlassEffectSpec = with(density) {
    LiquidGlassEffectSpec(
        blurPx = small1.toPx(),
        lensWidthPx = medium1.toPx(),
        lensHeightPx = medium3.toPx()
    )
}

fun DrawScope.drawLiquidGlassSurface(
    surfaceAlpha: Float = LiquidGlassDefaults.COMPACT_SURFACE_ALPHA,
    overlayAlpha: Float = LiquidGlassDefaults.COMPACT_OVERLAY_ALPHA,
    surfaceColor: Color = Color.White
) {
    drawRect(surfaceColor.copy(alpha = surfaceAlpha))
    drawRect(surfaceColor.copy(alpha = overlayAlpha), blendMode = BlendMode.Overlay)
}

fun DrawScope.drawLiquidGlassCardSurface(surfaceColor: Color = Color.White) {
    drawLiquidGlassSurface(
        surfaceAlpha = LiquidGlassDefaults.CARD_SURFACE_ALPHA,
        overlayAlpha = LiquidGlassDefaults.CARD_OVERLAY_ALPHA,
        surfaceColor = surfaceColor
    )
}
