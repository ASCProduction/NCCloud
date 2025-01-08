package tk.shkabaj.android.shkabaj.modules.mainscreen

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.bookmarks.NewsBookmarksManager
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCityEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountry
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecastInfo
import tk.shkabaj.android.shkabaj.network.entity.weather.toEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.toUiModel
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.utils.Platform
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

data class BallinaScreenState(
    val news: List<NewsItemModel> = emptyList(),
    val cryptoList: List<CryptoInfo> = emptyList(),
    val weatherState: BallinaWeatherState? = null,
    val progress: Boolean = false,
)

data class BallinaWeatherState(
    val countries: List<WeatherCountry> = emptyList(),
    val selectedCountry: WeatherCountry? = null,
    val selectedCity: WeatherCity? = null,
)

sealed interface BallinaScreenAction {
    data class ShowError(val error: Throwable): BallinaScreenAction
    data object NavigateToSettingsScreen: BallinaScreenAction
    data object NavigateToAudio: BallinaScreenAction
}

sealed interface BallinaScreenEvent {
    data class OnCountrySelected(val country: WeatherCountry) : BallinaScreenEvent
    data class OnCitySelected(val city: WeatherCity): BallinaScreenEvent
    data object OnRefresh : BallinaScreenEvent
    data class OnOpenUrl(val url: String): BallinaScreenEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): BallinaScreenEvent
}

class MainScreenViewModel(
    private val apiService: NCCloudApiService,
    private val countriesManager: CountriesManager,
    private val newsBookmarksManager: NewsBookmarksManager,
    private val platform: Platform,
) : BaseViewModel<BallinaScreenState, BallinaScreenAction, BallinaScreenEvent>(
    initialState = BallinaScreenState()
) {

    init {
        loadData()
        launchBookmarksCollecting()
    }

    private fun launchBookmarksCollecting() {
        screenModelScope.launch {
            newsBookmarksManager.getBookmarks().collectLatest { _ ->
                val newsList = viewState.news.map { newsItem ->
                    newsItem.copy(isBookmarked = newsBookmarksManager.isInBookmarks(newsItem.entity))
                }
                updateViewState { state ->
                    state.copy(news = newsList)
                }
            }
        }
    }

    override fun obtainEvent(event: BallinaScreenEvent) {
        when (event) {
            is BallinaScreenEvent.OnCountrySelected -> handleCountryClick(event.country)
            is BallinaScreenEvent.OnCitySelected -> handleCityClick(event.city.toEntity())
            is BallinaScreenEvent.OnRefresh -> loadData()
            is BallinaScreenEvent.OnOpenUrl -> platform.openUrl(url = event.url)
            is BallinaScreenEvent.OnToolbarEvent -> handleToolbarEvent(toolbarAction = event.toolbarAction)
        }
    }

    private fun loadData() {
        updateViewState { state -> state.copy(progress = true) }

        screenModelScope.launchSafe(
            onAction = {
                val weatherResponse = async { loadWeatherData() }
                val newsResponse = async { loadNews() }
                val cryptoResponse = async { loadCrypto() }

                val weatherState = weatherResponse.await()
                val news = newsResponse.await()
                val crypto = cryptoResponse.await()

                updateViewState { state ->
                    state.copy(
                        news = news,
                        weatherState = weatherState,
                        cryptoList = crypto,
                        progress = false
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun handleCountryClick(country: WeatherCountry) {
        updateViewState { state ->
            val newCountry = state.weatherState?.countries?.firstOrNull { it.name == country.name }
            state.copy(
                weatherState = state.weatherState?.copy(
                    selectedCountry = newCountry,
                    selectedCity = newCountry?.selectedCity
                ),
            )
        }
    }

    private fun handleCityClick(city: WeatherCityEntity) {
        screenModelScope.launchSafe(
            onAction = {
                val forecast = getForecastByCity(city)
                val selectedCity = city.toUiModel(forecast)
                updateViewState { state ->
                    state.copy(
                        weatherState = state.weatherState?.copy(
                            selectedCountry = state.weatherState.selectedCountry?.copy(
                                selectedCity = selectedCity
                            ),
                            selectedCity = selectedCity,
                            countries = state.weatherState.countries.map { country ->
                                if (country.name == state.weatherState.selectedCountry?.name) {
                                    country.copy(selectedCity = selectedCity)
                                } else {
                                    country
                                }
                            },
                        )
                    )
                }
            },
            onError = ::handleError
        )
    }

    private suspend fun loadNews(): List<NewsItemModel> {
        val response = apiService.getNews()
        return response
            .filter { it.sliderMobi }
            .map { item ->
                NewsItemModel(
                    entity = item,
                    isBookmarked = newsBookmarksManager.isInBookmarks(item)
                )
            }
    }

    private suspend fun loadCrypto(): List<CryptoInfo> {
        val response = apiService.getCrypto()
        return response
    }

    private suspend fun loadWeatherData(): BallinaWeatherState {
        val countries = apiService.getWeatherCountries().map { country ->
            val selectedCity = country.cities.firstOrNull { it.req == country.defaultCity }
            val forecast = selectedCity?.let { city -> getForecastByCity(city) }
            country.toUiModel(selectedCity?.toUiModel(forecast))
        }

        return BallinaWeatherState(
            selectedCountry = getSelectedCountry(countries),
            selectedCity = getSelectedCity(countries),
            countries = countries,
        )
    }

    private fun getSelectedCountry(countries: List<WeatherCountry>): WeatherCountry? {
        return viewState.weatherState?.selectedCountry ?: countries.firstOrNull { country ->
            country.name == countriesManager.getSelectedCountryName()
        } ?: countries.firstOrNull()
    }

    private fun getSelectedCity(countries: List<WeatherCountry>): WeatherCity? {
        return viewState.weatherState?.selectedCity ?: countries.firstOrNull()?.selectedCity
    }

    private suspend fun getForecastByCity(city: WeatherCityEntity): WeatherForecastInfo {
        return apiService.getForecastByCity(city).weatherForecastInfo
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = BallinaScreenAction.ShowError(error = error))
            updateViewState { state ->
                state.copy(progress = false)
            }
        }
    }

    private fun openPlayingRadio() {
        screenModelScope.launch {
            sendViewAction(action = BallinaScreenAction.NavigateToAudio)
        }
    }

    private fun handleToolbarEvent(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.SETTINGS -> sendViewAction(action = BallinaScreenAction.NavigateToSettingsScreen)
            ToolbarAction.PLAYER -> openPlayingRadio()
            else -> {}
        }
    }

}