package tk.shkabaj.android.shkabaj.network.entity.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCountryEntity(
    val cities: List<WeatherCityEntity>,
    val defaultCity: String,
    val name: String,
    val req: String
)

data class WeatherCountry(
    val name: String,
    val req: String,
    val cities: List<WeatherCity>,
    val defaultCity: String,
    val selectedCity: WeatherCity? = null
)

fun WeatherCountryEntity.toUiModel(selectedCity: WeatherCity?): WeatherCountry {
    return WeatherCountry(
        name = this.name,
        req = this.req,
        cities = this.cities.map { it.toUiModel() },
        defaultCity = this.defaultCity,
        selectedCity = selectedCity
    )
}