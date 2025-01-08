package tk.shkabaj.android.shkabaj.network.entity.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherShortForecast(
    @SerialName("date") val date: String,
    @SerialName("day") val day: String,
    @SerialName("dayName") val dayName: String,
    @SerialName("dt") val dt: String,
    @SerialName("hourly") val hourlyForecast: List<WeatherHourlyForecast>,
    @SerialName("mn") val mn: String,
    @SerialName("month") val month: String,
    @SerialName("wd") val wd: String,
    @SerialName("weekday") val weekday: String
)