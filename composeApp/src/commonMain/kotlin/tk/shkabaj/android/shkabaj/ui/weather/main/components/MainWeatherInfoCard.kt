package tk.shkabaj.android.shkabaj.ui.weather.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.StringTemperatureSuffix
import nccloud.composeapp.generated.resources.WeatherFeelsLike
import nccloud.composeapp.generated.resources.geolocation
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.extensions.thenIf
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.CarolinaBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.OnlyWhite
import tk.shkabaj.android.shkabaj.ui.theme.OrangeWeatherColor
import tk.shkabaj.android.shkabaj.ui.theme.TextBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography
import tk.shkabaj.android.shkabaj.ui.theme.WeatherBlueColor
import tk.shkabaj.android.shkabaj.ui.weather.main.WeatherProperty
import tk.shkabaj.android.shkabaj.ui.weather.main.mainProperties
import tk.shkabaj.android.shkabaj.ui.weather.main.sunriseProperty
import tk.shkabaj.android.shkabaj.ui.weather.main.sunsetProperty
import tk.shkabaj.android.shkabaj.utils.DateTimeUtils

val WeatherInfoCardDefaultPadding = PaddingValues(
    top = 32.dp,
    bottom = 22.dp,
    start = 16.dp,
    end = 16.dp
)

@Composable
fun MainWeatherInfoCard(
    city: WeatherCity,
    forecast: WeatherForecast,
    drawBackground: Boolean = true,
    paddingValues: PaddingValues = WeatherInfoCardDefaultPadding,
    onClick: (() -> Unit)? = null
) {
    val cardBGColor = CarolinaBlueColor

    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .thenIf(drawBackground) {
                background(
                    color = cardBGColor,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
            }
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(2f)) {
                Row(
                    modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = city.name,
                        style = Typography.headlineLarge.copy(
                            color = TextBlueColor,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Image(
                        painter = painterResource(Res.drawable.geolocation),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = DarkGreyColor)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = DateTimeUtils.formatWeatherDateTime(dt = city.todayForecast?.dt),
                    style = Typography.bodyLarge.copy(color = DarkGreyColor)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.StringTemperatureSuffix, forecast.temperature),
                    style = Typography.headlineLarge.copy(
                        color = TextBlueColor,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        Res.string.WeatherFeelsLike,
                        city.todayForecast?.feelsLikeC ?: 0
                    ),
                    style = Typography.bodyLarge.copy(color = DarkGreyColor)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = WeatherBlueColor,
                        shape = RoundedCornerShape(4.dp)
                    ).padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherPropertyContent(forecast.sunsetProperty)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = DividerNewsColor,
                    thickness = 0.5.dp
                )
                WeatherPropertyContent(forecast.sunriseProperty)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            forecast.mainProperties.forEach { property ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = OrangeWeatherColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp)
                ) {
                    WeatherPropertyContent(property)
                }
            }
        }
    }
}

@Composable
fun WeatherPropertyContent(weatherProperty: WeatherProperty) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(weatherProperty.icon),
            colorFilter = ColorFilter.tint(weatherProperty.iconTint),
            contentDescription = null
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = weatherProperty.title,
            style = Typography.bodySmall.copy(
                color = OnlyWhite,
                lineHeight = 17.sp,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = weatherProperty.subtitle,
            style = Typography.labelMedium.copy(
                color = OnlyWhite,
                lineHeight = 14.sp,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}