package ru.asmelnikov.person_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.composables.liquid.LiquidButtonBox
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun PersonHeader(
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
    name: String,
    teamUrl: String,
    onBackClick: () -> Unit
) {

    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                horizontal = dimens.medium1,
                vertical = dimens.small3
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LiquidButtonBox(
            modifier = Modifier
                .size(dimens.medium5),
            onClick = onBackClick,
            backdrop = backdrop
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(0.8f),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(dimens.small2))
        LiquidButtonBox(
            modifier = Modifier
                .height(dimens.medium4)
                .weight(1f),
            onClick = {},
            backdrop = backdrop
        ) {
            Text(
                modifier = Modifier.padding(horizontal = dimens.extraSmall2).basicMarquee(Int.MAX_VALUE),
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(dimens.small2))
        LiquidButtonBox(
            modifier = Modifier
                .size(dimens.medium5),
            onClick = {},
            backdrop = backdrop
        ) {
            SubComposeAsyncImageCommon(
                modifier = Modifier.fillMaxSize(0.7f),
                imageUri = teamUrl,
                shape = RoundedCornerShape(0.dp)
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun PersonHeaderPreview() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            PersonHeader(
                backdrop = rememberLayerBackdrop { },
                name = "Some name Some name Some name Some name",
                teamUrl = "",
                onBackClick = {}
            )
        }
    }
}