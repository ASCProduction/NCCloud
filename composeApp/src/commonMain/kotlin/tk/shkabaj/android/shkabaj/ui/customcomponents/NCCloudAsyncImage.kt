package tk.shkabaj.android.shkabaj.ui.customcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import org.jetbrains.compose.resources.painterResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_image_error_small
import nccloud.composeapp.generated.resources.ic_image_error_small_dark
import tk.shkabaj.android.shkabaj.managers.ThemeManager
import tk.shkabaj.android.shkabaj.ui.theme.LoadingCircleColor

@Composable
fun NCCloudAsyncImage(model: Any?,
                      contentScale: ContentScale = ContentScale.Fit,
                      modifier: Modifier,
                      verticalCropFactor: Float = 1f,
                      onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null) {
    SubcomposeAsyncImage(
        model = model,
        modifier = modifier
            .graphicsLayer {
                scaleX = 1f
                scaleY = verticalCropFactor
            },
        contentScale = contentScale,
        contentDescription = null,
        onSuccess = onSuccess,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .size(22.dp),
                color = LoadingCircleColor,
            )
        },
        error = {
            Image(
                painter = painterResource(
                    resource = if (ThemeManager.isDarkThemeEnabled()) {
                        Res.drawable.ic_image_error_small_dark
                    } else {
                        Res.drawable.ic_image_error_small
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier
            )
        }
    )
}