package tk.shkabaj.android.shkabaj.modules.settings

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCountryEntity
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction
import tk.shkabaj.android.shkabaj.utils.Platform

data class SettingsState(
    val weatherCountry: String? = null,
    val startTab: TabItem? = null,
    val theme: AppTheme? = null,
)

sealed interface SettingsAction {
    data class ShowError(val error: Throwable): SettingsAction
    data object PopupNavigation: SettingsAction
}

sealed interface SettingsEvent {
    data object OnDefaultCountryChanged : SettingsEvent
    data object OnDefaultScreenChanged: SettingsEvent
    data object OnDefaultThemeChanged: SettingsEvent
    data object OnCheckAvailableCountries: SettingsEvent
    data object ClearCache: SettingsEvent
    data object ShareApp: SettingsEvent
    data object SendEmail: SettingsEvent
    data class OnOpenUrl(val url: String): SettingsEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): SettingsEvent
}

class SettingsViewModel(
    private val countriesManager: CountriesManager,
    private val settingsManager: SettingsManager,
    private val platform: Platform
) : BaseViewModel<SettingsState, SettingsAction, SettingsEvent>(
    initialState = SettingsState()
) {

    init {
        screenModelScope.launchSafe(
            onAction = {
                val weatherCountry = countriesManager.getSelectedCountryName()
                val startTab = settingsManager.getStartTab()
                val selectedTheme = settingsManager.getSelectedTheme()
                updateViewState { state ->
                    state.copy(
                        weatherCountry = weatherCountry,
                        startTab = startTab,
                        theme = selectedTheme
                    )
                }
            },
            onError = ::handleError
        )
    }

    private suspend fun getCountries(): List<WeatherCountryEntity> {
        return countriesManager.countries.ifEmpty {
            countriesManager.loadCountries()
        }
    }

    private fun checkAvailableCountries() = screenModelScope.launch {
        val countries = getCountries()
        var defaultCountry = countries.firstOrNull { country ->
            country.name == countriesManager.getSelectedCountryName()
        }
        if(defaultCountry?.name == viewState.weatherCountry) return@launch

        if (defaultCountry == null && countries.isNotEmpty()) {
            defaultCountry = countries.first()
            countriesManager.setSelectedCountryName(defaultCountry.name)
        }
        updateViewState { state ->
            state.copy(
                weatherCountry = defaultCountry?.name
            )
        }
    }

    private fun clearAppCache() {
        //TODO: clear coil and ktor cache??
        screenModelScope.launch {
            platform.clearCache()
        }
    }

    private fun shareApp() {
        platform.shareApp()
    }

    private fun sendEmail() {
        platform.sendSupportEmail()
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.BACK_NAVIGATION -> sendViewAction(action = SettingsAction.PopupNavigation)
            else -> {}
        }
    }

    override fun obtainEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnDefaultCountryChanged -> {
                val countries = countriesManager.countries
                val defaultCountry = countries.firstOrNull { country ->
                    country.name == countriesManager.getSelectedCountryName()
                }
                updateViewState { state ->
                    state.copy(weatherCountry = defaultCountry?.name)
                }
            }
            SettingsEvent.OnDefaultScreenChanged -> {
                val tab = settingsManager.getStartTab()
                updateViewState { state ->
                    state.copy(startTab = tab)
                }
            }
            SettingsEvent.OnDefaultThemeChanged -> {
                val theme = settingsManager.getSelectedTheme()
                updateViewState { state ->
                    state.copy(theme = theme)
                }
            }
            SettingsEvent.OnCheckAvailableCountries -> checkAvailableCountries()
            SettingsEvent.ClearCache -> clearAppCache()
            SettingsEvent.ShareApp -> shareApp()
            SettingsEvent.SendEmail -> sendEmail()
            is SettingsEvent.OnOpenUrl -> {
                platform.openUrl(event.url)
            }
            is SettingsEvent.OnToolbarEvent -> handleToolbarAction(toolbarAction = event.toolbarAction)
        }
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = SettingsAction.ShowError(error = error))
        }
    }
}