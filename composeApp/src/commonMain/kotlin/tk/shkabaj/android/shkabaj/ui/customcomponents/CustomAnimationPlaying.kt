package tk.shkabaj.android.shkabaj.ui.customcomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor

@Composable
fun CustomAnimationPlaying(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(height = 20.dp).width(width = 36.dp)
            .padding(end = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick.invoke()
            }
    ) {
        Row(
            modifier = Modifier.width(width = 18.dp).height(height = 18.dp)
                .align(alignment = Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { _ ->
                AnimatedBox()
            }
        }
    }
}

@Composable
private fun AnimatedBox() {
    val randomRange = 6..18
    val animatedHeight = remember { Animatable(randomRange.random().toFloat()) }

    LaunchedEffect(Unit) {
        while (true) {
            val nextHeight = randomRange.random().toFloat()
            animatedHeight.animateTo(
                targetValue = nextHeight,
                animationSpec = tween(
                    durationMillis = (500..1000).random(),
                    easing = LinearOutSlowInEasing
                )
            )
            animatedHeight.animateTo(
                targetValue = 3.toFloat(),
                animationSpec = tween(
                    durationMillis = (500..1000).random(),
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .width(2.dp)
            .height(animatedHeight.value.dp)
            .background(color = AccentColor)
    )
}