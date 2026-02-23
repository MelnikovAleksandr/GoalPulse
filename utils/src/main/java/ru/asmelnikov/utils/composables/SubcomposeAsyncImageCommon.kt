package ru.asmelnikov.utils.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SubComposeAsyncImageCommon(
    modifier: Modifier = Modifier,
    imageUri: Any,
    shape: Shape,
    alpha: Float = 1f,
    size: Dp = dimens.champLogoDefaultSize,
    contentDescription: String? = null,
    errorPlaceHolder: Int = R.drawable.placeholder_photo,
    loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = {
        CircularProgressIndicator()
    },
    contentScale: ContentScale = ContentScale.FillHeight
) {

    if (isInPreview()) {
        Image(
            painter = painterResource(R.drawable.preview_image),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .size(size)
                .clip(shape)
        )
    } else {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            loading = loading,
            error = {
                Image(painter = painterResource(errorPlaceHolder), contentDescription = null)
            },
            alpha = alpha,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .size(size)
                .clip(shape)
        )
    }
}

@Composable
fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}