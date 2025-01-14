package tk.shkabaj.android.shkabaj.network

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ParametersBuilder
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCityEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountriesResponse
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountryEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecastResponse
import tk.shkabaj.android.shkabaj.network.utils.XmlParser

class NCCloudApiService(
    private val appNetworkClient: NetworkClient,
    private val commonNetworkClient: NetworkClient,
) {

    suspend fun getWeatherCountries(): List<WeatherCountryEntity> {
        return appNetworkClient.getRequest<WeatherCountriesResponse>(
            url = NetworkConfig.WEATHER_COUNTRIES_LIST_PATH
        ).countries
    }

    suspend fun getForecastByCity(city: WeatherCityEntity): WeatherForecastResponse {
        return appNetworkClient.getRequest<WeatherForecastResponse>(
            url = NetworkConfig.WEATHER_FORECAST_PATH,
            parametersBuilder = ParametersBuilder().apply {
                append("a", "city")
                append("c", city.req)
            }
        )
    }

    suspend fun getNews(): List<NewsEntity> {
        val response = appNetworkClient.getRequest<HttpResponse>(url = NetworkConfig.NEWS_PATH).bodyAsText()
        return XmlParser.parseNews(response).newsList
    }

    suspend fun getCrypto(): List<CryptoInfo> {
        val getUrlCrypto = urlForGetCryptoInfo()
        return commonNetworkClient.getCryptoRequest<List<CryptoInfo>>(
            url = getUrlCrypto.first,
            parametersBuilder = getUrlCrypto.second
        )
    }

    private fun urlForGetCryptoInfo(): Pair<String, ParametersBuilder> {
        val baseUrl = "https://api.coingecko.com/api/v3/coins/markets"
        val parameters = ParametersBuilder().apply {
            append("vs_currency", "usd")
        }
        return Pair(baseUrl, parameters)
    }

}