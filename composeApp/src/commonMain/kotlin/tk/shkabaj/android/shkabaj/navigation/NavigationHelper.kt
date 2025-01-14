package tk.shkabaj.android.shkabaj.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem

class NavigationHelper {

    sealed class Action {
        data class OpenNewsTab(val tabIndex: Int): Action()
        data class OpenWeatherTab(val tabIndex: Int): Action()
        data class OpenTab(val tab: TabItem): Action()
        data class OpenLink(val link: String, val insideApp: Boolean): Action()
        data class ShowPushDialog(val title: String, val handler: () -> Unit): Action()
    }

    private val _state = MutableStateFlow<Action?>(null)
    val state: StateFlow<Action?> = _state.asStateFlow()

    fun showPushDialog(title: String, handler: () -> Unit) {
        _state.update { Action.ShowPushDialog(title, handler) }
    }

    fun openNews(tabIndex: Int) {
        openTab(TabItem.NEWS)
        _state.update { Action.OpenNewsTab(tabIndex) }
    }

    fun openWeather(tabIndex: Int) {
        openTab(TabItem.WEATHER)
        _state.update { Action.OpenWeatherTab(tabIndex) }
    }

    fun openTab(tab: TabItem) {
        _state.update { Action.OpenTab(tab) }
    }

}