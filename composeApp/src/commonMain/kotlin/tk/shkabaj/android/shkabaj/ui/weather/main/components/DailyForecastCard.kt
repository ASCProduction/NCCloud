package tk.shkabaj.android.shkabaj.ui.weather.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Day
import nccloud.composeapp.generated.resources.Evening
import nccloud.composeapp.generated.resources.FeelsLikeTitle
import nccloud.composeapp.generated.resources.Morning
import nccloud.composeapp.generated.resources.Night
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.StringPercentSuffix
import nccloud.composeapp.generated.resources.StringTemperatureSuffix
import nccloud.composeapp.generated.resources.ic_weather_rainy_20
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.extensions.replaceDays
import tk.shkabaj.android.shkabaj.extensions.replaceMonths
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherHourlyForecast
import tk.shkabaj.android.shkabaj.ui.theme.DividerColorColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.LightCobaltBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.TextWhiteColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun DailyForecastCard(forecast: WeatherForecast) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(color = LightCobaltBlueColor, shape = RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Text(
            text = forecast.weekDayString.replaceDays().replaceMonths(),
            style = Typography.headlineSmall.copy(
                color = TextWhiteColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = DividerNewsColor,
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DailyForecastItem(forecast.hourlyForecast[0], Res.string.Morning)
            DailyForecastItem(forecast.hourlyForecast[1], Res.string.Day)
            DailyForecastItem(forecast.hourlyForecast[2], Res.string.Evening)
            DailyForecastItem(forecast.hourlyForecast[3], Res.string.Night)
        }
    }
}

@Composable
private fun DailyForecastItem(forecast: WeatherHourlyForecast, title: StringResource) {
    Column(
        modifier = Modifier.widthIn(min = 68.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(title),
            style = Typography.bodySmall.copy(
                color = TextWhiteColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        forecast.icon?.let { icon ->
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = null,
            )
        }

        Text(
            text = stringResource(Res.string.StringTemperatureSuffix, forecast.temperature),
            style = Typography.headlineSmall.copy(
                color = TextWhiteColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
        )

        Text(
            text = stringResource(Res.string.FeelsLikeTitle, forecast.feelsLikeC),
            style = Typography.labelSmall.copy(
                color = TextWhiteColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_weather_rainy_20),
                contentDescription = null,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                modifier = Modifier.heightIn(min = 15.dp),
                text = stringResource(Res.string.StringPercentSuffix, forecast.chanceOfRain),
                style = Typography.bodySmall.copy(
                    color = TextWhiteColor,
                    lineHeightStyle = LineHeightStyle.CenteredNonTrim
                ),
            )
        }
    }
}