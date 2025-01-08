package tk.shkabaj.android.shkabaj.ui.weather.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.StringTemperatureSuffix
import nccloud.composeapp.generated.resources.WeaklyWeatherTitle
import nccloud.composeapp.generated.resources.ic_baseline_more_vert_24
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.extensions.replaceDays
import tk.shkabaj.android.shkabaj.extensions.replaceMonths
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.BarMaximumColor
import tk.shkabaj.android.shkabaj.ui.theme.BarMinimumColor
import tk.shkabaj.android.shkabaj.ui.theme.BlueWithAlphaColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerColorColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.TextWhiteColor
import tk.shkabaj.android.shkabaj.ui.theme.TextWhiteWithAlphaColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun WeeklyForecastCard(
    forecasts: List<WeatherForecast>,
    onClick: (WeatherForecast?) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(color = BlueWithAlphaColor, shape = RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.WeaklyWeatherTitle),
                style = Typography.headlineSmall.copy(
                    color = TextWhiteColor,
                    lineHeightStyle = LineHeightStyle.CenteredNonTrim
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onClick.invoke(null) },
                modifier = Modifier.size(20.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_baseline_more_vert_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = DividerColorColor)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = DividerNewsColor,
            thickness = 0.5.dp
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            forecasts.forEachIndexed { index, forecast ->
                WeeklyForecastItem(
                    forecast = forecast,
                    onClick = { onClick.invoke(forecast) }
                )
                if (index != forecasts.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = DividerColorColor,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyForecastItem(forecast: WeatherForecast, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = forecast.weekday?.replaceDays()?.replaceMonths() ?: "",
            style = Typography.headlineSmall.copy(
                color = TextWhiteColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        forecast.icon?.let { icon ->
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier.widthIn(140.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.StringTemperatureSuffix, forecast.minTemperature),
                style = Typography.headlineSmall.copy(
                    color = TextWhiteWithAlphaColor,
                    lineHeightStyle = LineHeightStyle.CenteredNonTrim
                ),
            )

            GradientProgressBar(progress = 0.6f)

            Text(
                text = stringResource(Res.string.StringTemperatureSuffix, forecast.maxTemperature),
                style = Typography.headlineSmall.copy(
                    color = TextWhiteColor,
                    lineHeightStyle = LineHeightStyle.CenteredNonTrim
                ),
            )
        }
    }
}

@Composable
private fun GradientProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .width(70.dp)
            .height(2.dp)
            .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth(fraction = progress.coerceAtLeast(0.1f))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            BarMinimumColor,
                            BarMaximumColor
                        )
                    )
                )
        )
    }
}