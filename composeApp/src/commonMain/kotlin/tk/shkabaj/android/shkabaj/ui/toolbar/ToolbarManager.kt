package tk.shkabaj.android.shkabaj.ui.toolbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tk.shkabaj.android.shkabaj.navigation.AppScreen

class ToolbarManager {

    private val _currentScreen = MutableStateFlow<AppScreen?>(null)
    val currentScreen: StateFlow<AppScreen?> = _currentScreen

    fun updateScreen(appScreen: AppScreen) {
        _currentScreen.value = appScreen
    }

}