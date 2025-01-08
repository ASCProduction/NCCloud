package tk.shkabaj.android.shkabaj.ui.weather.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.WeatherConditions
import nccloud.composeapp.generated.resources.ic_close
import nccloud.composeapp.generated.resources.ic_weather_sunny_20
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.RadioPlayerActionColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun TopRow(closeSheet: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier.size(20.dp).align(alignment = Alignment.CenterVertically),
            painter = painterResource(Res.drawable.ic_weather_sunny_20),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            text = stringResource(Res.string.WeatherConditions),
            style = Typography.headlineSmall.copy(
                color = DarkGreyColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            onClick = closeSheet
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = null,
                tint = RadioPlayerActionColor
            )
        }
    }
}