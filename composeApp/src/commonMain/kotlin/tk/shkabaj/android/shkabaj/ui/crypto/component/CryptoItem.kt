package tk.shkabaj.android.shkabaj.ui.crypto.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.extensions.androidAllowHardware
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.customcomponents.NCCloudAsyncImage
import tk.shkabaj.android.shkabaj.ui.theme.CulturedGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun CryptoItem(
    cryptoItem: CryptoInfo,
    modifier: Modifier
) {
    var size by remember { mutableStateOf(Size.Zero) }
    val scope = rememberCoroutineScope()
    val loader = rememberPainterLoader()
    val dominantColorState = rememberDominantColorState(
        loader = loader,
        defaultColor = CulturedGreyColor
    )

    val animatedColor by animateColorAsState(
        targetValue = dominantColorState.color,
        animationSpec = tween(durationMillis = 750)
    )
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth().height(height = 185.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    size = layoutCoordinates.size.toSize()
                }
                .background(
                    brush = if (size != Size.Zero) {
                        Brush.radialGradient(
                            colors = listOf(
                                animatedColor,
                                CulturedGreyColor
                            ),
                            center = Offset(size.width / 2, size.height / 2 - 55),
                            radius = size.minDimension - 135f
                        ) } else {
                        Brush.linearGradient(
                            colors = listOf(CulturedGreyColor, CulturedGreyColor)
                        )
                    },
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            NCCloudAsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(cryptoItem.image)
                    .androidAllowHardware(false)
                    .build(),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(95.dp).fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp)
                    .align(alignment = Alignment.Center)
                    .clip(RoundedCornerShape(size = 4.dp)),
                onSuccess = { item ->
                    scope.launch {
                        dominantColorState.updateFrom(item.painter)
                    }
                }
            )
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = cryptoItem.name ?: "",
            style = Typography.headlineLarge.copy(
                color = MaastrichtBlueColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = cryptoItem.currentPrice.toString(),
            style = Typography.headlineLarge.copy(
                color = OldSilverColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}