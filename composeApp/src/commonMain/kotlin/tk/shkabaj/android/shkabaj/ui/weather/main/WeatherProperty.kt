package tk.shkabaj.android.shkabaj.ui.weather.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Humidity
import nccloud.composeapp.generated.resources.Precipitation
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.StringPercentSuffix
import nccloud.composeapp.generated.resources.Sunrise
import nccloud.composeapp.generated.resources.Sunset
import nccloud.composeapp.generated.resources.Visibility
import nccloud.composeapp.generated.resources.Wind
import nccloud.composeapp.generated.resources.ic_weather_airwave_32
import nccloud.composeapp.generated.resources.ic_weather_rainy_32
import nccloud.composeapp.generated.resources.ic_weather_sunrise_32
import nccloud.composeapp.generated.resources.ic_weather_twilight_32
import nccloud.composeapp.generated.resources.ic_weather_visibility_32
import nccloud.composeapp.generated.resources.ic_weather_waterdrop_32
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.OrangeColor

data class WeatherProperty(
    val title: String,
    val subtitle: String,
    val icon: DrawableResource,
    val iconTint: Color = Color.White
)

val WeatherForecast.humidityProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = stringResource(Res.string.StringPercentSuffix, humidity),
        subtitle = stringResource(Res.string.Humidity),
        icon = Res.drawable.ic_weather_waterdrop_32
    )

val WeatherForecast.windProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = windString,
        subtitle = stringResource(Res.string.Wind),
        icon = Res.drawable.ic_weather_airwave_32
    )

val WeatherForecast.visibilityProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = visibilityString,
        subtitle = stringResource(Res.string.Visibility),
        icon = Res.drawable.ic_weather_visibility_32
    )

val WeatherForecast.sunsetProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = sunset,
        subtitle = stringResource(Res.string.Sunset),
        icon = Res.drawable.ic_weather_twilight_32,
        iconTint = OrangeColor
    )

val WeatherForecast.sunriseProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = sunrise,
        subtitle = stringResource(Res.string.Sunrise),
        icon = Res.drawable.ic_weather_sunrise_32,
        iconTint = OrangeColor
    )

val WeatherForecast.rainChanceProperty: WeatherProperty
    @Composable
    get() = WeatherProperty(
        title = stringResource(Res.string.StringPercentSuffix, chanceOfRain),
        subtitle = stringResource(Res.string.Precipitation),
        icon = Res.drawable.ic_weather_rainy_32,
    )

val WeatherForecast.mainProperties: List<WeatherProperty>
    @Composable
    get() = listOf(humidityProperty, windProperty, visibilityProperty)

val WeatherForecast.detailProperties: List<WeatherProperty>
    @Composable
    get() = listOf(windProperty, rainChanceProperty, humidityProperty)