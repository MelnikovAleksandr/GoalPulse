package ru.asmelnikov.utils.composables.liquid

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Elevation
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorMaxDistance
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
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
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.dimens

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
        BallRefreshIndicator(
            progress = state.distanceFraction,
            isRefreshing = isRefreshing
        )
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
    val liquidColor = MaterialTheme.colorScheme.primary

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
private fun BallRefreshIndicator(
    progress: Float,
    isRefreshing: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ball_refresh_spin")
    val spinRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1688, easing = LinearEasing)
        ),
        label = "spin"
    )

    val pullRotation = progress.coerceAtLeast(0f) * 360f
    val rotation = if (isRefreshing) spinRotation else pullRotation

    Image(
        painter = painterResource(id = R.drawable.ball_loader),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .size(dimens.medium2)
            .clearAndSetSemantics {
                when {
                    isRefreshing -> {
                        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
                    }
                    progress > 0f -> {
                        progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f, 0)
                    }
                }
            }
            .rotate(rotation)
    )
}

private const val SCALE_GROWTH_FACTOR = 1.75f

private fun scaleForPullProgress(pullProgress: Float): Float {
    return (pullProgress.coerceIn(0f, 1f) * SCALE_GROWTH_FACTOR).coerceAtMost(1f)
}