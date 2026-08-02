package ru.asmelnikov.team_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.composables.liquid.LiquidBox
import ru.asmelnikov.utils.composables.liquid.LiquidButtonBox
import ru.asmelnikov.utils.composables.liquid.drawProgressivePlainBackdropReverse
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CollapsingToolbarScope.Toolbar(
    collapsingState: CollapsingToolbarScaffoldState,
    mainColor: Color,
    secondColor: Color,
    teamName: String,
    teamCrest: String,
    onBackClick: () -> Unit
) {

    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }
    val startSize = dimens.medium4.value
    val density = LocalDensity.current
    val blurRadiusPx = with(density) { dimens.medium2.toPx() }

    val topInset = with(density) { WindowInsets.systemBars.getTop(this).toDp() }


    val progress = collapsingState.toolbarState.progress

    val collapsedBottomInset = ((1f - progress) * 8f).dp
    val minBarHeight = topInset + startSize.dp + collapsedBottomInset + dimens.extraSmall2

    val textSize by remember(progress) {
        derivedStateOf { (18 + (18 * progress)).sp }
    }

    val imgSize by remember(progress) {
        derivedStateOf { (startSize + (100 * progress)).dp }
    }

    Box(
        modifier = Modifier
            .layerBackdrop(backdrop)
            .parallax()
            .pin()
            .fillMaxWidth()
            .size(dimens.emptyContentImageSize)
            .pin()
            .background(mainColor)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minBarHeight)
            .road(
                whenCollapsed = Alignment.BottomCenter,
                whenExpanded = Alignment.BottomCenter
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.medium4)
                .drawProgressivePlainBackdropReverse(
                    backdrop = backdrop,
                    blurRadiusPx = blurRadiusPx,
                    tint = secondColor
                )
        )
    }

    Box(
        modifier = Modifier
            .heightIn(min = minBarHeight)
            .statusBarsPadding()
            .padding(horizontal = dimens.small1)
            .road(
                whenCollapsed = Alignment.TopEnd,
                whenExpanded = Alignment.Center
            ),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier.size(imgSize),
            contentAlignment = Alignment.Center
        ) {
            LiquidBox(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        alpha = ((1f - progress) * 3f).coerceIn(0f, 1f)
                    },
                backdrop = backdrop
            ) {}
            SubComposeAsyncImageCommon(
                modifier = Modifier.fillMaxSize(0.7f),
                imageUri = teamCrest,
                shape = RoundedCornerShape(0.dp)
            )
        }
    }

    Text(
        text = teamName,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = textSize,
        style = TextStyle(
            shadow = Shadow(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                offset = Offset(0f, 1f),
                blurRadius = 4f
            )
        ),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .heightIn(min = minBarHeight)
            .statusBarsPadding()
            .padding(horizontal = dimens.medium1)
            .padding(
                bottom = dimens.medium1,
                top = dimens.extraSmall2
            )
            .road(
                whenCollapsed = Alignment.TopCenter,
                whenExpanded = Alignment.BottomCenter
            )
    )

    Box(
        modifier = Modifier
            .heightIn(min = minBarHeight)
            .road(
                whenCollapsed = Alignment.TopStart,
                whenExpanded = Alignment.TopStart
            ),
        contentAlignment = Alignment.TopStart
    ) {
        LiquidButtonBox(
            modifier = Modifier
                .systemBarsPadding()
                .padding(horizontal = dimens.small1)
                .size(startSize.dp),
            onClick = onBackClick,
            backdrop = backdrop
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(0.8f),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}