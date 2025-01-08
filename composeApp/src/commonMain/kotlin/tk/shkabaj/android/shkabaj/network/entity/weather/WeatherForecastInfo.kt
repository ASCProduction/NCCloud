package tk.shkabaj.android.shkabaj.network.entity.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecastInfo(
    @SerialName("currentConditions") val currentConditions: WeatherForecast,
    @SerialName("forecast") val forecasts: List<WeatherForecast>,
    @SerialName("short") val short: List<WeatherShortForecast>,
    @SerialName("short_forecast") val shortForecast: List<WeatherForecast>
)