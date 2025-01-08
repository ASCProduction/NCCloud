package tk.shkabaj.android.shkabaj.network.entity.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import tk.shkabaj.android.shkabaj.utils.CommonUtils

@Serializable
data class WeatherHourlyForecast(
    @SerialName("chanceofrain") val chanceOfRain: String,
    @SerialName("cloudcover") val cloudCover: String,
    @SerialName("description") val description: String,
    @SerialName("diffRad") val diffRad: String,
    @SerialName("FeelsLikeC") val feelsLikeC: String,
    @SerialName("FeelsLikeF") val feelsLikeF: String,
    @SerialName("humidity") val humidity: String,
    @SerialName("iconUrl") val iconUrl: String,
    @SerialName("iconUrlExt") val iconUrlExt: String,
    @SerialName("isdaytime") val isDayTime: String,
    @SerialName("partOfDay") val partOfDay: String,
    @SerialName("pressure") val pressure: String,
    @SerialName("shortRad") val shortRad: String,
    @SerialName("temperature") val temperature: String,
    @SerialName("time") val time: String,
    @SerialName("uvIndex") val uvIndex: String,
    @SerialName("visibility") val visibility: String,
    @SerialName("windDirection") val windDirection: String,
    @SerialName("windSpeed") val windSpeed: String
) {
    val icon: DrawableResource?
        get() {
            val name = CommonUtils.getIconNameFromUrl(iconUrl)
            return CommonUtils.getDrawableByName(name)
        }
}