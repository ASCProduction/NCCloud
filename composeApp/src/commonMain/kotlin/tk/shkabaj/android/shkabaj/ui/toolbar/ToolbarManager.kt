package tk.shkabaj.android.shkabaj.ui.toolbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.player.NowPlayingManager

class ToolbarManager(nowPlayingManager: NowPlayingManager) {

    private val _currentScreen = MutableStateFlow<AppScreen?>(null)
    val currentScreen: StateFlow<AppScreen?> = _currentScreen

    private val _showNewsFilter = MutableStateFlow(false)
    val showNewsFilter: StateFlow<Boolean> = _showNewsFilter

    val nowPlayingState = nowPlayingManager.state

    fun updateScreen(appScreen: AppScreen) {
        _currentScreen.value = appScreen
    }

    fun updateShowingNewsFilter(isShowFilter: Boolean) {
        _showNewsFilter.value = isShowFilter
    }

}