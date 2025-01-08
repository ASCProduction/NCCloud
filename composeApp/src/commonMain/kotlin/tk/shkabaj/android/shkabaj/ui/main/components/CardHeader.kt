package tk.shkabaj.android.shkabaj.ui.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.More
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_audio_arrow
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.Afacad
import tk.shkabaj.android.shkabaj.ui.theme.TitleBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun CardHeader(
    title: String,
    showMore: Boolean = true,
    onShowMoreClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.Bottom),
            text = title,
            style = TextStyle(
                fontFamily = Afacad,
                fontWeight = FontWeight.SemiBold,
                fontSize = 40.sp,
                color = TitleBlueColor
            )
        )

        if (showMore) {
            Row(
                modifier = Modifier.align(alignment = Alignment.Bottom)
                    .clickable {
                        onShowMoreClick()
                    }
            ) {
                TextButton(
                    modifier = Modifier.heightIn(max = 26.dp)
                        .padding(bottom = 10.dp),
                    contentPadding = PaddingValues(),
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(Res.string.More),
                        style = Typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = AccentColor
                        ),
                    )
                }
                Image(
                    painter = painterResource(resource = Res.drawable.ic_audio_arrow),
                    colorFilter = ColorFilter.tint(color = AccentColor),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 10.dp)
                        .width(16.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}
