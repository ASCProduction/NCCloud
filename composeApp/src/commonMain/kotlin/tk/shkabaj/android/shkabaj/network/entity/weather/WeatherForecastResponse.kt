package tk.shkabaj.android.shkabaj.network.entity.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecastResponse(
    @SerialName("data") val weatherForecastInfo: WeatherForecastInfo,
    @SerialName("result") val result: String
)