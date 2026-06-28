package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop

@Composable
fun LiquidPullToRefreshWrapper(
    backdrop: Backdrop,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    topOffset: Dp = 0.dp,
    contentAlignment: Alignment = Alignment.TopStart,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    Box(
        modifier.pullToRefresh(
            state = refreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            enabled = enabled
        ),
        contentAlignment = contentAlignment
    ) {
        content()
        LiquidRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            state = refreshState,
            isRefreshing = isRefreshing,
            backdrop = backdrop,
            topOffset = topOffset
        )
    }
}
