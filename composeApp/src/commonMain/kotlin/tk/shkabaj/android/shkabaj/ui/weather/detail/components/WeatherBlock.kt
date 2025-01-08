package tk.shkabaj.android.shkabaj.ui.weather.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.MaxTemperatureTitle
import nccloud.composeapp.generated.resources.MinTemperatureTitle
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_weather_temperature_32
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.BlueColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.OrangeWeatherColor
import tk.shkabaj.android.shkabaj.ui.theme.TemperatureMinColor
import tk.shkabaj.android.shkabaj.ui.theme.TextWhiteColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography
import tk.shkabaj.android.shkabaj.ui.weather.main.components.WeatherPropertyContent
import tk.shkabaj.android.shkabaj.ui.weather.main.detailProperties
import tk.shkabaj.android.shkabaj.ui.weather.main.sunriseProperty
import tk.shkabaj.android.shkabaj.ui.weather.main.sunsetProperty

@Composable
fun WeatherBlock(forecast: WeatherForecast) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherPropertyBox(
                modifier = Modifier.weight(1f),
                padding = 12.5.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(Res.drawable.ic_weather_temperature_32),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = null
                    )
                    Spacer(Modifier.height(4.dp))
                    Row {
                        Text(
                            modifier = Modifier.padding(end = 2.dp),
                            text = stringResource(
                                Res.string.MaxTemperatureTitle,
                                forecast.maxTemperature
                            ),
                            style = Typography.headlineLarge.copy(
                                color = TextWhiteColor,
                                lineHeightStyle = LineHeightStyle.CenteredNonTrim
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(start = 2.dp),
                            text = stringResource(
                                Res.string.MinTemperatureTitle,
                                forecast.minTemperature
                            ),
                            style = Typography.headlineLarge.copy(
                                color = TemperatureMinColor,
                                lineHeightStyle = LineHeightStyle.CenteredNonTrim
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .weight(2.1f)
                    .background(
                        color = BlueColor,
                        shape = RoundedCornerShape(4.dp)
                    ).padding(vertical = 8.dp, horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherPropertyContent(forecast.sunriseProperty)
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = DividerNewsColor,
                    thickness = .5.dp
                )
                WeatherPropertyContent(forecast.sunsetProperty)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            forecast.detailProperties.forEach { property ->
                WeatherPropertyBox(modifier = Modifier.weight(1f),) {
                    WeatherPropertyContent(property)
                }
            }
        }
    }
}

@Composable
fun WeatherPropertyBox(
    modifier: Modifier = Modifier,
    padding: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = OrangeWeatherColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(padding)
    ) {
        content()
    }
}