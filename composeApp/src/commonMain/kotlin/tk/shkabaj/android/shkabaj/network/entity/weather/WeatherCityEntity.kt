package tk.shkabaj.android.shkabaj.network.entity.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCityEntity(
    val name: String,
    val req: String
)

data class WeatherCity(
    val name: String,
    val req: String,
    val todayForecast: WeatherForecast? = null,
    val tomorrowForecast: WeatherForecast? = null,
    val nowForecast: WeatherForecast? = null,
    val forecasts: List<WeatherForecast> = emptyList()
) {
    val weekForecasts: List<WeatherForecast>
        get() = listOfNotNull(todayForecast, tomorrowForecast) + forecasts
}

fun WeatherCity.toEntity(): WeatherCityEntity {
    return WeatherCityEntity(name, req)
}

fun WeatherCityEntity.toUiModel(forecastInfo: WeatherForecastInfo? = null): WeatherCity {
    val short = forecastInfo?.short ?: emptyList()
    val shortForecasts = forecastInfo?.shortForecast ?: emptyList()
    val todayForecast = shortForecasts.firstOrNull()?.copy(
        hourlyForecast = short.firstOrNull()?.hourlyForecast ?: emptyList()
    )

    // INFO: short forecasts filled with hourly data
    val filledShortForecasts = mutableListOf<WeatherForecast>()

    shortForecasts.forEachIndexed { index, forecast ->
        if (index == 1) {
            val secondHourlyForecast = short.getOrNull(index)?.hourlyForecast ?: emptyList()
            filledShortForecasts.add(forecast.copy(hourlyForecast = secondHourlyForecast))
        } else {
            filledShortForecasts.add(forecast)
        }
    }

    val tomorrowForecast = filledShortForecasts.getOrNull(index = 1)

    return WeatherCity(
        name = this.name,
        req = this.req,
        nowForecast = forecastInfo?.currentConditions,
        todayForecast = todayForecast,
        tomorrowForecast = tomorrowForecast,
        forecasts = forecastInfo?.forecasts ?: emptyList()
    )
}
