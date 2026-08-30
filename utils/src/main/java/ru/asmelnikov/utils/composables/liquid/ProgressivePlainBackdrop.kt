package ru.asmelnikov.utils.composables.liquid

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.runtimeShaderEffect

private const val PROGRESSIVE_BLUR_ALPHA_MASK_SHADER = """
    uniform shader content;

    uniform float2 size;
    layout(color) uniform half4 tint;
    uniform float tintIntensity;

    half4 main(float2 coord) {
        float blurAlpha = smoothstep(size.y, size.y * 0.5, coord.y);
        float tintAlpha = smoothstep(size.y, size.y * 0.5, coord.y);
        return mix(content.eval(coord) * blurAlpha, tint * tintAlpha, tintIntensity);
    }
"""

private const val PROGRESSIVE_BLUR_ALPHA_MASK_REVERSE_SHADER = """
    uniform shader content;

    uniform float2 size;
    layout(color) uniform half4 tint;
    uniform float tintIntensity;

    half4 main(float2 coord) {
        float blurAlpha = smoothstep(size.y * 0.5, size.y, coord.y);
        float tintAlpha = smoothstep(size.y * 0.5, size.y, coord.y);
        return mix(content.eval(coord) * blurAlpha, tint * tintAlpha, tintIntensity);
    }
"""

fun Modifier.drawProgressivePlainBackdrop(
    backdrop: Backdrop,
    blurRadiusPx: Float,
    tint: Color,
    tintIntensity: Float = 1f,
    shape: Shape = RectangleShape,
): Modifier = graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawPlainBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            blur(blurRadiusPx)
            runtimeShaderEffect(
                key = "AlphaMask",
                shaderString = PROGRESSIVE_BLUR_ALPHA_MASK_SHADER,
                uniformShaderName = "content",
            ) {
                setFloatUniform("size", size.width, size.height)
                setColorUniform("tint", tint)
                setFloatUniform("tintIntensity", tintIntensity)
            }
        },
    )

fun Modifier.drawProgressivePlainBackdropReverse(
    backdrop: Backdrop,
    blurRadiusPx: Float,
    tint: Color,
    tintIntensity: Float = 1f,
    shape: Shape = RectangleShape,
): Modifier = graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawPlainBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            blur(blurRadiusPx)
            runtimeShaderEffect(
                key = "AlphaMaskReverse",
                shaderString = PROGRESSIVE_BLUR_ALPHA_MASK_REVERSE_SHADER,
                uniformShaderName = "content",
            ) {
                setFloatUniform("size", size.width, size.height)
                setColorUniform("tint", tint)
                setFloatUniform("tintIntensity", tintIntensity)
            }
        },
    )
