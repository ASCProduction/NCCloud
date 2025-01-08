package tk.shkabaj.android.shkabaj.managers

import tk.shkabaj.android.shkabaj.data.local.PreferencesKey
import tk.shkabaj.android.shkabaj.data.local.PreferencesManager
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountryEntity

class CountriesManager(
    private val preferences: PreferencesManager,
    private val apiService: NCCloudApiService
) {

    var countries: List<WeatherCountryEntity> = emptyList()
        private set

    suspend fun loadCountries(): List<WeatherCountryEntity> {
        val data = apiService.getWeatherCountries()
        countries = data
        return countries
    }

    fun setSelectedCountryName(country: String) {
        preferences.putString(PreferencesKey.WEATHER_COUNTRY, country)
    }

    fun getSelectedCountryName(): String? {
        return preferences.getStringOrNull(PreferencesKey.WEATHER_COUNTRY)
    }
}