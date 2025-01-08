package tk.shkabaj.android.shkabaj.modules.main

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.managers.SearchBarManager
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.managers.UpdatesManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.notifications.NavigationHelper
import tk.shkabaj.android.shkabaj.ui.main.MainScreen
import tk.shkabaj.android.shkabaj.ui.news.NewsBookmarksScreen
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager
import tk.shkabaj.android.shkabaj.ui.news.NewsScreen
import tk.shkabaj.android.shkabaj.ui.settings.SettingsScreen
import tk.shkabaj.android.shkabaj.utils.Platform
import tk.shkabaj.android.shkabaj.utils.analytics.AnalyticsTracker

data class MainUIState (
    val initialTab: TabItem,
    val toolbarManager: ToolbarManager,
    val isSearchBarFocused: Boolean = false,
    val isScreenWithSearchBar: Boolean = false,
    val tabsWithUpdates: List<TabItem> = emptyList(),
    val isHideBottomBar: Boolean = false
)

sealed interface MainAction {
    data class ShowError(val error: Throwable): MainAction
    data class HandleNavigationAction(val action: NavigationHelper.Action): MainAction
}

sealed interface MainEvent

class MainViewModel(
    private val navigationHelper: NavigationHelper,
    private val updatesManager: UpdatesManager,
    settingsManager: SettingsManager,
    private val toolbarManager: ToolbarManager,
    private val searchBarManager: SearchBarManager,
    private val platform: Platform
): BaseViewModel<MainUIState, MainAction, MainEvent>(
    initialState = MainUIState(initialTab = settingsManager.getStartTab(), toolbarManager)
) {

    init {
        launchNavigationHelperCollecting()
        launchSearchBarCollecting()
        launchCurrentScreenCollecting()
        launchTabsUpdatesCheck()
        startUpdatesCheckTimer()
    }

    fun openUrl(url: String) {
        platform.openUrl(url)
    }

    private fun launchNavigationHelperCollecting() {
        screenModelScope.launch {
            navigationHelper.state.collect { event ->
                event?.let {
                    sendViewAction(action = MainAction.HandleNavigationAction(it))
                }
            }
        }
    }

    private fun launchSearchBarCollecting() {
        screenModelScope.launch {
            searchBarManager.isSearchBarFocused.collect { isFocused ->
                updateViewState { state ->
                    state.copy(isSearchBarFocused = isFocused)
                }
            }
        }
    }

    private fun launchCurrentScreenCollecting() {
        screenModelScope.launch {
            toolbarManager.currentScreen.collect { appScreen ->
                val tabsWithUpdates = viewState.tabsWithUpdates.toMutableList()
                when (appScreen) {
                    is NewsScreen -> tabsWithUpdates.remove(TabItem.NEWS)
                    else -> {}
                }
                when (appScreen) {
                    is MainScreen -> AnalyticsTracker.trackBallinaScreen()
                    is NewsBookmarksScreen -> AnalyticsTracker.trackNewsBookmarksScreen()
                    is SettingsScreen -> AnalyticsTracker.trackSettingsScreen()
                    else -> {}
                }
                updateViewState { state ->
                    state.copy(
                        tabsWithUpdates = tabsWithUpdates,
                    )
                }
            }
        }
    }

    private fun launchTabsUpdatesCheck() {
        screenModelScope.launch {
            try {
                val tabsWithUpdates = withContext(Dispatchers.IO) {
                    updatesManager.checkUpdates()
                }
                updateViewState { state ->
                    state.copy(tabsWithUpdates = tabsWithUpdates)
                }
            } catch (e: Exception) {
                println("launchUpdatesCheck error $e")
            }
        }
    }

    private fun startUpdatesCheckTimer() {
        screenModelScope.launch {
            while(isActive) {
                delay(1000 * 60 * 60) //Waiting 1 hour for checking updates of news/radio/video
                launchTabsUpdatesCheck()
            }
        }
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = MainAction.ShowError(error))
        }
    }

}
