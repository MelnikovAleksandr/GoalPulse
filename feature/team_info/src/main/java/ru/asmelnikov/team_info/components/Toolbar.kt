package ru.asmelnikov.team_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CollapsingToolbarScope.Toolbar(
    collapsingState: CollapsingToolbarScaffoldState,
    teamName: String,
    teamCrest: String,
    lightMutedSwatch: String,
    onDarkVibrant: String,
    isMaterialColors: Boolean
) {
    val progress = collapsingState.toolbarState.progress
    val textSize by remember(progress) {
        derivedStateOf { (18 + (18 * progress)).sp }
    }

    val imgSize by remember(progress) {
        derivedStateOf { (40 + (100 * progress)).dp }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .pin()
            .background(
                color = if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else Color(
                    lightMutedSwatch.toColorInt()
                )
            )
    )

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(
                horizontal = dimens.small1
            )
            .road(
                whenCollapsed = Alignment.TopEnd,
                whenExpanded = Alignment.Center
            )
            .background(
                color = Color.White.copy(
                    alpha = 1f - collapsingState.toolbarState.progress
                ),
                shape = CircleShape
            )
    ) {


        SubComposeAsyncImageCommon(
            imageUri = teamCrest,
            shape = RoundedCornerShape(0.dp),
            size = imgSize
        )

    }

    Text(
        text = teamName,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = textSize,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = dimens.medium1)
            .padding(
                bottom = dimens.medium1
            )
            .road(
                whenCollapsed = Alignment.TopCenter,
                whenExpanded = Alignment.BottomCenter
            ),
        color = Color(onDarkVibrant.toColorInt())
    )
}