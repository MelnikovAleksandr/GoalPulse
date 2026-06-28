package ru.asmelnikov.competitions_main.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Elevation
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorMaxDistance
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import ru.asmelnikov.utils.composables.liquid.LiquidGlassDefaults
import ru.asmelnikov.utils.composables.liquid.drawLiquidGlassSurface
import ru.asmelnikov.utils.composables.liquid.rememberLiquidGlassCompactSpec
import ru.asmelnikov.utils.ui.theme.dimens
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

@Composable
fun LiquidRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    topOffset: Dp = 0.dp,
    maxDistance: Dp = IndicatorMaxDistance
) {
    LiquidRefreshIndicatorBox(
        modifier = modifier,
        state = state,
        isRefreshing = isRefreshing,
        backdrop = backdrop,
        topOffset = topOffset,
        maxDistance = maxDistance
    ) {
        Crossfade(
            targetState = isRefreshing,
            label = "liquid_refresh_indicator"
        ) { refreshing ->
            if (refreshing) {
                CircularProgressIndicator(
                    strokeWidth = dimens.pullRefreshIndicatorStrokeWidth,
                    color = Color.Black,
                    modifier = Modifier.size(dimens.medium1)
                )
            } else {
                CircularArrowProgressIndicator(
                    progress = state.distanceFraction,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun LiquidRefreshIndicatorBox(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    topOffset: Dp = 0.dp,
    maxDistance: Dp = IndicatorMaxDistance,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = PullToRefreshDefaults.indicatorShape
    val glassSpec = rememberLiquidGlassCompactSpec()
    val liquidColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = modifier
            .size(dimens.medium4)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeWithLayer(0, 0) {
                        val pullProgress = state.distanceFraction.coerceAtLeast(0f)
                        val scale = when {
                            isRefreshing -> 1f
                            else -> scaleForPullProgress(pullProgress)
                        }
                        val topOffsetPx = topOffset.roundToPx()
                        val revealDistancePx = maxDistance.roundToPx()

                        transformOrigin = TransformOrigin(0.5f, 0f)
                        scaleX = scale
                        scaleY = scale
                        translationY = topOffsetPx + pullProgress * revealDistancePx
                        alpha = when {
                            isRefreshing -> 1f
                            scale <= 0f -> 0f
                            else -> scale
                        }

                        val showElevation = pullProgress > 0f || isRefreshing
                        shadowElevation = if (showElevation) Elevation.toPx() else 0f
                        this.shape = shape
                        clip = true
                    }
                }
            }
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(glassSpec.blurPx)
                    lens(glassSpec.lensWidthPx, glassSpec.lensHeightPx)
                },
                onDrawSurface = {
                    drawLiquidGlassSurface(
                        surfaceAlpha = LiquidGlassDefaults.CARD_SURFACE_ALPHA,
                        overlayAlpha = LiquidGlassDefaults.CARD_OVERLAY_ALPHA,
                        surfaceColor = liquidColor
                    )
                }
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun CircularArrowProgressIndicator(
    progress: Float,
    color: Color
) {
    val path = remember { Path().apply { fillType = PathFillType.EvenOdd } }
    val iconSize = dimens.medium1
    val strokeWidth = dimens.pullRefreshIndicatorStrokeWidth
    val arrowSize = dimens.small2

    Canvas(
        modifier = Modifier
            .clearAndSetSemantics {
                if (progress > 0f) {
                    progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f, 0)
                }
            }
            .size(iconSize)
    ) {
        val values = ArrowValues(progress)
        rotate(degrees = values.rotation) {
            val arcRadius = iconSize.toPx() * 0.34375f + strokeWidth.toPx() / 2f
            val arcBounds = Rect(center = size.center, radius = arcRadius)
            drawCircularIndicator(color, values, arcBounds, strokeWidth)
            drawArrow(path, arcBounds, color, values, strokeWidth, arrowSize)
        }
    }
}

private fun DrawScope.drawArrow(
    arrow: Path,
    bounds: Rect,
    color: Color,
    values: ArrowValues,
    strokeWidth: Dp,
    arrowSize: Dp
) {
    arrow.reset()
    arrow.moveTo(0f, 0f)
    arrow.lineTo(x = arrowSize.toPx() * values.scale / 2, y = arrowSize.toPx() * values.scale)
    arrow.lineTo(x = arrowSize.toPx() * values.scale, y = 0f)

    val radius = min(bounds.width, bounds.height) / 2f
    val inset = arrowSize.toPx() * values.scale / 2f
    arrow.translate(
        Offset(x = radius + bounds.center.x - inset, y = bounds.center.y - strokeWidth.toPx())
    )
    rotate(degrees = values.endAngle - strokeWidth.toPx()) {
        drawPath(path = arrow, color = color, style = Stroke(strokeWidth.toPx()))
    }
}

private fun DrawScope.drawCircularIndicator(
    color: Color,
    values: ArrowValues,
    arcBounds: Rect,
    strokeWidth: Dp
) {
    drawArc(
        color = color,
        startAngle = values.startAngle,
        sweepAngle = values.endAngle - values.startAngle,
        useCenter = false,
        topLeft = arcBounds.topLeft,
        size = arcBounds.size,
        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
    )
}

@Immutable
private class ArrowValues(
    val rotation: Float,
    val startAngle: Float,
    val endAngle: Float,
    val scale: Float
)

private const val SCALE_GROWTH_FACTOR = 1.75f

private fun scaleForPullProgress(pullProgress: Float): Float {
    return (pullProgress.coerceIn(0f, 1f) * SCALE_GROWTH_FACTOR).coerceAtMost(1f)
}

private fun ArrowValues(progress: Float): ArrowValues {
    val adjustedPercent = max(min(1f, progress) - 0.4f, 0f) * 5 / 3
    val overshootPercent = abs(progress) - 1.0f
    val linearTension = overshootPercent.coerceIn(0f, 2f)
    val tensionPercent = linearTension - linearTension.pow(2) / 4

    val endTrim = adjustedPercent * 0.8f
    val rotation = (-0.25f + 0.4f * adjustedPercent + tensionPercent) * 0.5f
    val startAngle = rotation * 360
    val endAngle = (rotation + endTrim) * 360
    val scale = min(1f, adjustedPercent)

    return ArrowValues(rotation, startAngle, endAngle, scale)
}
