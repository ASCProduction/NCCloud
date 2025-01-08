package tk.shkabaj.android.shkabaj.network.entity.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCountriesResponse(
    val countries: List<WeatherCountryEntity>
)