package tk.shkabaj.android.shkabaj.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import nccloud.composeapp.generated.resources.ChangeCity
import nccloud.composeapp.generated.resources.City
import nccloud.composeapp.generated.resources.Res
import tk.shkabaj.android.shkabaj.extensions.currentBottomSheetNavigator
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.mainscreen.BallinaScreenAction
import tk.shkabaj.android.shkabaj.modules.mainscreen.BallinaScreenEvent
import tk.shkabaj.android.shkabaj.modules.mainscreen.MainScreenViewModel
import tk.shkabaj.android.shkabaj.navigation.tabs.WeatherTab
import tk.shkabaj.android.shkabaj.ui.common.SearchBottomSheet
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.main.components.BallinaWeatherCard
import tk.shkabaj.android.shkabaj.ui.main.components.NewsCard
import tk.shkabaj.android.shkabaj.ui.settings.SettingsScreen
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.navigation.tabs.NewsTab
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.main.components.CryptoCard
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class MainScreen : AppScreen {

    override val title: AppScreenTitle? = null
    override val toolbarActions: List<ToolbarAction>
        get() = listOf(ToolbarAction.PLAYER, ToolbarAction.SETTINGS)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<MainScreenViewModel>()
        val state by viewModel.viewStates().collectAsState()
        val tabNavigator = LocalTabNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = currentBottomSheetNavigator
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = BallinaScreenEvent.OnToolbarEvent(toolbarAction = toolbarAction))
            }
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when (uiAction) {
                    is BallinaScreenAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    is BallinaScreenAction.NavigateToSettingsScreen -> navigator.push(SettingsScreen())
                    else -> {}
                }
            }
        }

        val onCryptoClick: (cryptoItem: CryptoInfo, isLong: Boolean) -> Unit = { cryptoItem, isLong ->
            //TODO:
        }

        val onCityClick: () -> Unit = {
            val screen = SearchBottomSheet(
                title = Res.string.ChangeCity,
                list = state.weatherState?.selectedCountry?.cities?.map { it.name } ?: emptyList(),
                initialString = state.weatherState?.selectedCity?.name ?: "",
                isSearch = true,
                searchTitle = Res.string.City,
                closeSheet = { cityName ->
                    state.weatherState?.selectedCountry?.cities
                        ?.firstOrNull { it.name == cityName }?.let { city ->
                            viewModel.obtainEvent(BallinaScreenEvent.OnCitySelected(city))
                        }
                    bottomSheetNavigator.hide()
                }
            )
            bottomSheetNavigator.show(screen)
        }

        PullToRefreshBox(
            isRefreshing = state.progress,
            onRefresh = { viewModel.obtainEvent(BallinaScreenEvent.OnRefresh) }
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MainBGColor)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.news.isNotEmpty()) {
                        NewsCard(
                            news = state.news,
                            onItemClick = {
                                tabNavigator.current = NewsTab
                            }
                        )
                    }

                    if(state.cryptoList.isNotEmpty()) {
                        CryptoCard(
                            cryptoList = state.cryptoList,
                            onCryptoClick = onCryptoClick
                        )
                    }

                    state.weatherState?.let { weatherState ->
                        BallinaWeatherCard(
                            state = weatherState,
                            onCardClick = {
                                tabNavigator.current = WeatherTab
                            },
                            onCityClick = onCityClick
                        )
                    }
                }
            }
        }
    }
}
