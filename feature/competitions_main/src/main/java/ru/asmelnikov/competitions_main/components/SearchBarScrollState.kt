package ru.asmelnikov.competitions_main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext

@Stable
class SearchBarScrollState(initialHideOffsetPx: Float = 0f) {
    var hideOffsetPx by mutableFloatStateOf(initialHideOffsetPx)
        private set

    private var maxHidePx = 0f
    private var snapJob: Job? = null

    fun updateMaxHidePx(heightPx: Float) {
        if (heightPx <= 0f || maxHidePx == heightPx) return
        maxHidePx = heightPx
        hideOffsetPx = hideOffsetPx.coerceIn(0f, maxHidePx)
    }

    fun onScroll(deltaPx: Float) {
        if (deltaPx == 0f) return
        snapJob?.cancel()
        snapJob = null
        if (maxHidePx <= 0f) return
        hideOffsetPx = (hideOffsetPx + deltaPx).coerceIn(0f, maxHidePx)
    }

    suspend fun snapToNearest() {
        if (maxHidePx <= 0f) return

        val start = hideOffsetPx
        val target = if (start >= maxHidePx / 2f) maxHidePx else 0f
        if (start == target) return

        snapJob?.cancel()
        snapJob = currentCoroutineContext()[Job]

        try {
            animate(
                initialValue = start,
                targetValue = target,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            ) { value, _ ->
                hideOffsetPx = value
            }
            hideOffsetPx = target
        } catch (_: CancellationException) {
            // User resumed scrolling — keep the current offset.
        } finally {
            snapJob = null
        }
    }

    fun expand() {
        snapJob?.cancel()
        snapJob = null
        hideOffsetPx = 0f
    }

    companion object {
        val Saver: Saver<SearchBarScrollState, Float> = Saver(
            save = { it.hideOffsetPx },
            restore = { SearchBarScrollState(initialHideOffsetPx = it) },
        )
    }
}

@Composable
fun rememberSearchBarScrollState(): SearchBarScrollState = rememberSaveable(
    saver = SearchBarScrollState.Saver,
) { SearchBarScrollState() }
