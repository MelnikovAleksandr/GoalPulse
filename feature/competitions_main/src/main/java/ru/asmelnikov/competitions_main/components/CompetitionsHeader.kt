package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlin.math.roundToInt
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.shapes.Capsule
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.liquid.LiquidButtonBox
import ru.asmelnikov.utils.composables.liquid.LiquidGlassDefaults
import ru.asmelnikov.utils.composables.liquid.drawLiquidGlassSurface
import ru.asmelnikov.utils.composables.liquid.rememberLiquidGlassCompactSpec
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CompetitionsHeader(
    backdrop: Backdrop,
    competitionsCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchBarScrollState: SearchBarScrollState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                horizontal = dimens.medium1,
                vertical = dimens.small3
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LiquidButtonBox(
                modifier = Modifier
                    .size(dimens.medium5),
                onClick = {},
                backdrop = backdrop
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape).fillMaxSize(0.8f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(dimens.medium1))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.available_competitions_count, competitionsCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
        ) {
            CollapsibleSearchBarSection(
                searchBarScrollState = searchBarScrollState
            ) {
                Spacer(modifier = Modifier.height(dimens.small3))

                CompetitionsSearchBar(
                    backdrop = backdrop,
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange
                )
            }
        }
    }
}

@Composable
private fun CollapsibleSearchBarSection(
    searchBarScrollState: SearchBarScrollState,
    content: @Composable () -> Unit
) {
    val sectionHeight = remember { SectionHeightHolder() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val hideOffsetPx = searchBarScrollState.hideOffsetPx
                val placeable = measurable.measure(constraints)
                if (sectionHeight.px == 0f) {
                    sectionHeight.px = placeable.height.toFloat()
                    searchBarScrollState.updateMaxHidePx(sectionHeight.px)
                }
                val progress = if (sectionHeight.px > 0f) {
                    (hideOffsetPx / sectionHeight.px).coerceIn(0f, 1f)
                } else {
                    0f
                }
                val scaleY = 1f - progress
                val visibleHeight = (sectionHeight.px * scaleY).roundToInt().coerceAtLeast(0)
                layout(placeable.width, visibleHeight) {
                    placeable.place(0, 0)
                }
            }
            .graphicsLayer {
                val hideOffsetPx = searchBarScrollState.hideOffsetPx
                val progress = if (sectionHeight.px > 0f) {
                    (hideOffsetPx / sectionHeight.px).coerceIn(0f, 1f)
                } else {
                    0f
                }
                transformOrigin = TransformOrigin(0.5f, 0f)
                scaleY = 1f - progress
                alpha = 1f - progress * 0.35f
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

private class SectionHeightHolder(var px: Float = 0f)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompetitionsSearchBar(
    backdrop: Backdrop,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (!isImeVisible) {
            focusManager.clearFocus()
        }
    }

    val glassSpec = rememberLiquidGlassCompactSpec()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.extraSmall2)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { Capsule() },
                effects = {
                    vibrancy()
                    blur(glassSpec.blurPx)
                    lens(glassSpec.lensWidthPx, glassSpec.lensHeightPx)
                },
                onDrawSurface = {
                    drawLiquidGlassSurface(
                        surfaceAlpha = LiquidGlassDefaults.COMPACT_SURFACE_ALPHA,
                        overlayAlpha = LiquidGlassDefaults.COMPACT_OVERLAY_ALPHA,
                        surfaceColor = Color.White
                    )
                }
            )
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                singleLine = true,
                enabled = true,
                visualTransformation = VisualTransformation.None,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.searchBarIconSize)
                    )
                },
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search),
                                modifier = Modifier.size(dimens.searchBarIconSize)
                            )
                        }
                    }
                } else {
                    null
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_competitions),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                shape = Capsule(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White.copy(alpha = 0.55f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                )
            )
        }
    }
}
