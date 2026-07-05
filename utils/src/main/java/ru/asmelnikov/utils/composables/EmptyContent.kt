package ru.asmelnikov.utils.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    withScroll: Boolean = false,
    onReloadClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (withScroll) {
                    Modifier.verticalScroll(rememberScrollState())
                } else {
                    Modifier
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimens.large))
        Image(
            modifier = Modifier.size(dimens.emptyContentImageSize),
            painter = painterResource(id = R.drawable.page_is_empty),
            contentDescription = null
        )
        TextButton(onClick = onReloadClick) {
            Text(
                text = stringResource(R.string.reload),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}