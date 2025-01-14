package tk.shkabaj.android.shkabaj.modules.weather

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCityEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountry
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecastInfo
import tk.shkabaj.android.shkabaj.network.entity.weather.toEntity
import tk.shkabaj.android.shkabaj.network.entity.weather.toUiModel
import tk.shkabaj.android.shkabaj.navigation.NavigationHelper
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

sealed interface WeatherEvent {
    data class OnCitySelected(val city: WeatherCity) : WeatherEvent
    data class OnCountrySelected(val country: WeatherCountry) : WeatherEvent
    data object OnRefresh : WeatherEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): WeatherEvent
}

sealed interface WeatherAction {
    data class ShowError(val error: Throwable): WeatherAction
    data object NavigateToSettings: WeatherAction
}

data class WeatherState(
    val countries: List<WeatherCountry> = emptyList(),
    val selectedCountry: WeatherCountry? = null,
    val selectedCity: WeatherCity? = null,
    val progress: Boolean = false,
)

class WeatherViewModel(
    private val apiService: NCCloudApiService,
    private val countriesManager: CountriesManager,
    private val navigationHelper: NavigationHelper
) : BaseViewModel<WeatherState, WeatherAction, WeatherEvent>(
    initialState = WeatherState()
) {

    private var pendingCountryIndex: Int? = null

    init {
        loadData()
        launchNavigationHelperCollecting()
    }

    private fun launchNavigationHelperCollecting() {
        screenModelScope.launch {
            navigationHelper.state.collect { event ->
                when(event) {
                    is NavigationHelper.Action.OpenWeatherTab -> {
                        pendingCountryIndex = event.tabIndex
                        selectPendingCountryIfNeeded()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun selectPendingCountryIfNeeded() {
        val index = pendingCountryIndex ?: return
        val country = viewState.countries.getOrNull(index) ?: return
        obtainEvent(WeatherEvent.OnCountrySelected(country))
        pendingCountryIndex = null
    }

    override fun obtainEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.OnCitySelected -> {
                updateViewState { state -> state.copy(progress = true) }
                updateSelectedCity(event.city.toEntity())
            }
            is WeatherEvent.OnCountrySelected -> {
                updateViewState { state ->
                    state.copy(
                        selectedCountry = event.country,
                        selectedCity = event.country.selectedCity
                    )
                }
            }
            is WeatherEvent.OnRefresh -> loadData()
            is WeatherEvent.OnToolbarEvent -> handleToolbarAction(toolbarAction = event.toolbarAction)
        }
    }

    private fun updateSelectedCity(city: WeatherCityEntity) {
        screenModelScope.launchSafe(
            onAction = {
                val forecast = getForecastByCity(city)
                val selectedCity = city.toUiModel(forecast)
                updateViewState { state ->
                    state.copy(
                        selectedCountry = state.selectedCountry?.copy(
                            selectedCity = selectedCity
                        ),
                        selectedCity = selectedCity,
                        countries = state.countries.map { country ->
                            if (country.name == state.selectedCountry?.name) {
                                country.copy(selectedCity = selectedCity)
                            } else {
                                country
                            }
                        },
                        progress = false
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun loadData() {
        screenModelScope.launchSafe(
            onAction = {
                updateViewState { state -> state.copy(progress = true) }

                val countries = getCountries().map { country ->
                    val selectedCity = country.cities.firstOrNull { it.req == country.defaultCity }
                    val forecast = selectedCity?.let { city -> getForecastByCity(city) }
                    country.toUiModel(selectedCity?.toUiModel(forecast))
                }
                updateViewState { state ->
                    state.copy(
                        selectedCountry = getSelectedCountry(countries),
                        selectedCity = getSelectedCity(countries),
                        countries = countries,
                        progress = false,
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun getSelectedCountry(countries: List<WeatherCountry>): WeatherCountry? {
        val pendingIndex = pendingCountryIndex
        val defaultBlock: () -> WeatherCountry? = {
            viewState.selectedCountry ?: countries.firstOrNull { country ->
                country.name == countriesManager.getSelectedCountryName()
            } ?: countries.firstOrNull()
        }
        if (pendingIndex != null) {
            pendingCountryIndex = null
            return countries.getOrNull(pendingIndex) ?: defaultBlock()
        } else {
            return defaultBlock()
        }
    }

    private fun getSelectedCity(countries: List<WeatherCountry>): WeatherCity? {
        return viewState.selectedCity ?: countries.firstOrNull()?.selectedCity
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.SETTINGS -> sendViewAction(action = WeatherAction.NavigateToSettings)
            else -> {}
        }
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = WeatherAction.ShowError(error = error))
            updateViewState { state ->
                state.copy(progress = false)
            }
        }
    }

    private suspend fun getCountries() = apiService.getWeatherCountries()

    private suspend fun getForecastByCity(city: WeatherCityEntity): WeatherForecastInfo {
        return apiService.getForecastByCity(city).weatherForecastInfo
    }

}