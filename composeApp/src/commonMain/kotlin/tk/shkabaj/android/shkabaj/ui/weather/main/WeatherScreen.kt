package tk.shkabaj.android.shkabaj.ui.weather.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nccloud.composeapp.generated.resources.ChangeCity
import nccloud.composeapp.generated.resources.City
import nccloud.composeapp.generated.resources.Res
import tk.shkabaj.android.shkabaj.extensions.currentBottomSheetNavigator
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.weather.WeatherAction
import tk.shkabaj.android.shkabaj.modules.weather.WeatherEvent
import tk.shkabaj.android.shkabaj.modules.weather.WeatherState
import tk.shkabaj.android.shkabaj.modules.weather.WeatherViewModel
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.common.SearchBottomSheet
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.settings.SettingsScreen
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction
import tk.shkabaj.android.shkabaj.ui.weather.detail.WeatherDetailBottomSheet
import tk.shkabaj.android.shkabaj.ui.weather.main.components.DailyForecastCard
import tk.shkabaj.android.shkabaj.ui.weather.main.components.MainWeatherInfoCard
import tk.shkabaj.android.shkabaj.ui.weather.main.components.WeeklyForecastCard

class WeatherScreen : AppScreen {

    override val title: AppScreenTitle? = null
    override val toolbarActions: List<ToolbarAction>
        get() = listOf(ToolbarAction.SETTINGS)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<WeatherViewModel>()
        val state by viewModel.viewStates().collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = currentBottomSheetNavigator
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = WeatherEvent.OnToolbarEvent(toolbarAction = toolbarAction))
            }
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when (uiAction) {
                    is WeatherAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    is WeatherAction.NavigateToSettings -> navigator.push(SettingsScreen())
                    else -> {}
                }
            }
        }

        val selectedCity = state.selectedCity

        PullToRefreshBox(
            isRefreshing = state.progress,
            onRefresh = { viewModel.obtainEvent(WeatherEvent.OnRefresh) },
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                Box(modifier = Modifier.fillMaxSize().background(color = MainBGColor)) {
                    MainContent(
                        state = state,
                        onWeatherDetailsClick = { forecast ->
                            val screen = WeatherDetailBottomSheet(
                                city = selectedCity,
                                selectedForecast = forecast,
                                forecasts = selectedCity?.weekForecasts ?: emptyList(),
                                closeSheet = bottomSheetNavigator::hide
                            )
                            bottomSheetNavigator.show(screen)
                        },
                        onCityClick = {
                            val screen = SearchBottomSheet(
                                title = Res.string.ChangeCity,
                                list = state.selectedCountry?.cities?.map { it.name }
                                    ?: emptyList(),
                                initialString = state.selectedCity?.name ?: "",
                                isSearch = true,
                                searchTitle = Res.string.City,
                                closeSheet = { cityName ->
                                    state.selectedCountry?.cities
                                        ?.firstOrNull { it.name == cityName }
                                        ?.let { city ->
                                            viewModel.obtainEvent(WeatherEvent.OnCitySelected(city))
                                        }
                                    bottomSheetNavigator.hide()
                                }
                            )
                            bottomSheetNavigator.show(screen)
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun MainContent(
        state: WeatherState,
        onCityClick: () -> Unit,
        onWeatherDetailsClick: (WeatherForecast?) -> Unit
    ) {
        val selectedCity = state.selectedCity ?: return

        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MainBGColor)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 8.dp)
            ) {
                selectedCity.nowForecast?.let { todayForecast ->
                    MainWeatherInfoCard(
                        city = selectedCity,
                        forecast = todayForecast,
                        onClick = onCityClick
                    )
                }

                selectedCity.todayForecast?.let { DailyForecastCard(forecast = it) }

                Spacer(Modifier.height(8.dp))

                selectedCity.tomorrowForecast?.let { DailyForecastCard(forecast = it) }

                Spacer(Modifier.height(8.dp))

                WeeklyForecastCard(
                    forecasts = selectedCity.weekForecasts,
                    onClick = onWeatherDetailsClick
                )
            }
        }
    }

}