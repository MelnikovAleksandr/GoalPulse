package ru.asmelnikov.competitions_main.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.isInPreview
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    val context = LocalContext.current
    Image(
        painter = if (isInPreview()) {
            painterResource(id = R.drawable.main_gif)
        } else {
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(data = R.drawable.main_gif)
                    .apply(block = { size(Size.ORIGINAL) })
                    .build(),
                imageLoader = ImageLoader.Builder(context)
                    .components {
                        if (Build.VERSION.SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build()
            )
        },
        contentDescription = null,
        modifier = modifier
            .height(dimens.mainGifHeight)
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
        alpha = alpha
    )
}