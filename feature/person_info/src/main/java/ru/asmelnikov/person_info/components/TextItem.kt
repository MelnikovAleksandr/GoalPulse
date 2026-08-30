package ru.asmelnikov.person_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.utils.composables.liquid.LiquidButtonRow
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
    title: String,
    text: String,
) {
    LiquidButtonRow(
        modifier = modifier,
        onClick = {},
        backdrop = backdrop,
    ) {
        Text(
            modifier = Modifier.padding(vertical = dimens.small2, horizontal = dimens.small2),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = dimens.small2, horizontal = dimens.small2)
                .basicMarquee(Int.MAX_VALUE),
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.End,
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TextItemPreview() {
    GoalPulseTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            TextItem(
                modifier = Modifier.padding(dimens.small3),
                backdrop = rememberLayerBackdrop { },
                title = "Name",
                text = "Some name Some name Some name Some name Some name Some name Some name",
            )
        }
    }
}
