package tk.shkabaj.android.shkabaj.notifications

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem

class NavigationHelper {

    sealed class Action {
        data class OpenLajmeTab(val tabIndex: Int): Action()
        data class OpenWeatherTab(val tabIndex: Int): Action()
        data class OpenRadio(val name: String): Action()
        data class OpenTV(val name: String): Action()
        data class OpenTab(val tab: TabItem): Action()
        data class OpenLink(val link: String, val insideApp: Boolean): Action()
        data class ShowPushDialog(val title: String, val handler: () -> Unit): Action()
    }

    private val _state = MutableStateFlow<Action?>(null)
    val state: StateFlow<Action?> = _state.asStateFlow()

    fun showPushDialog(title: String, handler: () -> Unit) {
        _state.update { Action.ShowPushDialog(title, handler) }
    }

    fun openLajme(tabIndex: Int) {
        openTab(TabItem.NEWS)
        _state.update { Action.OpenLajmeTab(tabIndex) }
    }

    fun openWeather(tabIndex: Int) {
        openTab(TabItem.WEATHER)
        _state.update { Action.OpenWeatherTab(tabIndex) }
    }

    fun openRadio(name: String) {
        openTab(TabItem.HOME)
        _state.update { Action.OpenRadio(name) }
    }

    fun openTV(name: String) {
        openTab(TabItem.HOME)
        _state.update { Action.OpenTV(name) }
    }

    fun openTab(tab: TabItem) {
        _state.update { Action.OpenTab(tab) }
    }

    fun openLink(link: String, insideApp: Boolean) {
        _state.update { Action.OpenLink(link, insideApp) }
    }

}