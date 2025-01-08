package tk.shkabaj.android.shkabaj.network.entity.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import tk.shkabaj.android.shkabaj.utils.CommonUtils
import tk.shkabaj.android.shkabaj.utils.DateTimeUtils

@Serializable
data class WeatherForecast(
    @SerialName("chanceofrain") val chanceOfRain: String,
    @SerialName("cloudcover") val cloudCover: String,
    @SerialName("date") val date: String? = null,
    @SerialName("day") val day: String? = null,
    @SerialName("description") val description: String,
    @SerialName("dt") val dt: String? = null,
    @SerialName("FeelsLikeC") val feelsLikeC: String,
    @SerialName("FeelsLikeF") val feelsLikeF: String,
    @SerialName("humidity") val humidity: String,
    @SerialName("iconUrl") val iconUrl: String,
    @SerialName("iconUrlExt") val iconUrlExt: String,
    @SerialName("maxTemperature") val maxTemperature: String,
    @SerialName("minTemperature") val minTemperature: String,
    @SerialName("mn") val mn: String? = null,
    @SerialName("month") val month: String? = null,
    @SerialName("moon_illumination") val moonIllumination: String,
    @SerialName("moon_phase") val moonPhase: String,
    @SerialName("moonrise") val moonrise: String,
    @SerialName("moonset") val moonSet: String,
    @SerialName("pressure") val pressure: String,
    @SerialName("sunrise") val sunrise: String,
    @SerialName("sunset") val sunset: String,
    @SerialName("temperature") val temperature: String,
    @SerialName("time") val time: String? = null,
    @SerialName("uvIndex") val uvIndex: String,
    @SerialName("visibility") val visibility: String,
    @SerialName("wd") val wd: String? = null,
    @SerialName("weekday") val weekday: String? = null,
    @SerialName("windDirection") val windDirection: String,
    @SerialName("windSpeed") val windSpeed: String,
    @SerialName("isdaytime") val isDayTime: String? = null,
    @SerialName("dayName") val dayName: String? = null,
    @SerialName("hourly") val hourlyForecast: List<WeatherHourlyForecast> = emptyList(),
) {
    val weekDayString: String
        get() = "$weekday, $day $mn"

    val windString: String
        get() = "$windSpeed $windDirection"

    val visibilityString: String
        get() = "$visibility km"

    val fullDateString: String
        get() = "$weekday, $day $month ${DateTimeUtils.getCurrentYear()}"

    val icon: DrawableResource?
        get() {
            val name = CommonUtils.getIconNameFromUrl(iconUrl)
            return CommonUtils.getDrawableByName(name)
        }
}