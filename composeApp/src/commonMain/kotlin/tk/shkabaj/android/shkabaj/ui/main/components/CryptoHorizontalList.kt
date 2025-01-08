package tk.shkabaj.android.shkabaj.ui.main.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import tk.shkabaj.android.shkabaj.ui.theme.RadioTextCardColor
import tk.shkabaj.android.shkabaj.ui.theme.ShadowColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun CryptoHorizontalList(
    cryptoList: List<CryptoInfo>,
    onCryptoClick: (cryptoItem: CryptoInfo, isLong: Boolean) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items(cryptoList) { cryptoItem ->
            CryptoItem(
                cryptoItem = cryptoItem,
                onCryptoClick = onCryptoClick
            )
        }
    }
}

@Composable
fun CryptoItem(
    cryptoItem: CryptoInfo,
    onCryptoClick: (cryptoItem: CryptoInfo, isLong: Boolean) -> Unit
) {
    Column {
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

        Card(
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(4.dp),
                    spotColor = ShadowColor
                ),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .height(177.dp)
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
                        }
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onCryptoClick.invoke(cryptoItem, false)
                    }
            ) {
                Spacer(Modifier.height(20.dp))

                NCCloudAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(cryptoItem.image)
                        .androidAllowHardware(false)
                        .build(),
                    contentScale = ContentScale.FillHeight,
                    onSuccess = { item ->
                        scope.launch {
                            dominantColorState.updateFrom(item.painter)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                ElevatedCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    colors = CardDefaults.elevatedCardColors().copy(containerColor = RadioTextCardColor)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        text = cryptoItem.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.bodySmall.copy(color = MaastrichtBlueColor),
                    )
                }
            }
        }
    }
}