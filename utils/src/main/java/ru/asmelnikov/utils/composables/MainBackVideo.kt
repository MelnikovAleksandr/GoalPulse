package ru.asmelnikov.utils.composables

import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import ru.asmelnikov.utils.R

private fun resourceUri(packageName: String, @RawRes resId: Int): String =
    "android.resource://$packageName/$resId"

@OptIn(UnstableApi::class)
@Composable
fun MainBackVideo(
    modifier: Modifier = Modifier,
    @RawRes videoResId: Int,
    @RawRes reverseVideoResId: Int,
    pingPong: Boolean = true
) {
    if (isInPreview()) {
        Image(
            painter = painterResource(R.drawable.main_back_video_preview),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clipToBounds()
                .fillMaxSize()
        )
        return
    }

    val context = LocalContext.current.applicationContext
    val packageName = context.packageName

    val exoPlayer = remember(videoResId, reverseVideoResId, pingPong) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                if (pingPong) {
                    setMediaItems(
                        listOf(
                            MediaItem.fromUri(resourceUri(packageName, videoResId)),
                            MediaItem.fromUri(resourceUri(packageName, reverseVideoResId))
                        )
                    )
                } else {
                    setMediaItem(MediaItem.fromUri(resourceUri(packageName, videoResId)))
                }
                repeatMode = Player.REPEAT_MODE_ALL
                volume = 0f
                prepare()
                playWhenReady = true
            }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    Box(modifier = modifier.clipToBounds()) {
        ContentFrame(
            player = exoPlayer,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
            contentScale = ContentScale.Crop
        )
    }
}
