package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.shapes.RoundedRectangle
import ru.asmelnikov.utils.composables.liquid.drawLiquidGlassCardSurface
import ru.asmelnikov.utils.composables.liquid.rememberLiquidGlassCardSpec
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun LiquidCompetitionCard(
    backdrop: LayerBackdrop,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val glassSpec = rememberLiquidGlassCardSpec()
    val liquidColor = MaterialTheme.colorScheme.onBackground
    val liquidCardCornerRadius = dimens.liquidCardCornerRadius
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedRectangle(liquidCardCornerRadius) },
                effects = {
                    vibrancy()
                    blur(glassSpec.blurPx)
                    lens(glassSpec.lensWidthPx, glassSpec.lensHeightPx)
                },
                onDrawSurface = {
                    drawLiquidGlassCardSurface(surfaceColor = liquidColor)
                }
            )
            .height(dimens.competitionCardHeight),
        content = content
    )
}
