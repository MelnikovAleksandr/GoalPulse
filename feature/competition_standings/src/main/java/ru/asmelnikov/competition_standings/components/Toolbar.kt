package ru.asmelnikov.competition_standings.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CollapsingToolbarScope.Toolbar(
    collapsingState: CollapsingToolbarScaffoldState,
    areaUrl: String,
    compUrl: String,
    compName: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    with(sharedTransitionScope) {
        val progress = collapsingState.toolbarState.progress

        val textSize by remember(progress) {
            derivedStateOf { (18 + (18 * progress)).sp }
        }

        val imgSize by remember(progress) {
            derivedStateOf { (40 + (100 * progress)).dp }
        }

        SubComposeAsyncImageCommon(
            imageUri = areaUrl,
            shape = RoundedCornerShape(0.dp),
            size = dimens.emptyContentImageSize,
            alpha = collapsingState.toolbarState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .parallax()
                .pin(),
            loading = {},
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = dimens.small1)
                .sharedElement(
                    rememberSharedContentState(
                        key = compUrl
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                )
                .road(
                    whenCollapsed = Alignment.TopEnd,
                    whenExpanded = Alignment.Center
                )
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = Color.White.copy(
                            alpha = 1f - collapsingState.toolbarState.progress
                        ),
                        shape = CircleShape
                    )
            ) {
                SubComposeAsyncImageCommon(
                    modifier = Modifier.padding(2.dp),
                    imageUri = compUrl,
                    shape = RoundedCornerShape(0.dp),
                    size = imgSize
                )
            }
        }

        Text(
            text = compName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = textSize,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
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
    }
}