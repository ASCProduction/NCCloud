package tk.shkabaj.android.shkabaj.modules.settings

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme
import tk.shkabaj.android.shkabaj.utils.Platform
import tk.shkabaj.android.shkabaj.managers.ThemeManager

data class SettingsThemeUIState(
    val selectedTheme: AppTheme? = null
)

sealed interface SettingsThemeAction {
    data class ShowError(val error: Throwable): SettingsThemeAction
}

sealed interface SettingsThemeEvent {
    data class OnSelectedNewTheme(val newTheme: AppTheme, val isDark: Boolean): SettingsThemeEvent
}

class SettingsThemeViewModel(
    private val settingsManager: SettingsManager,
    private val platform: Platform
): BaseViewModel<SettingsThemeUIState, SettingsThemeAction, SettingsThemeEvent>(
    initialState = SettingsThemeUIState()
) {

    init {
        screenModelScope.launchSafe(
            onAction = {
                val selectedTheme = settingsManager.getSelectedTheme()
                updateViewState { state ->
                    state.copy(
                        selectedTheme = selectedTheme
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = SettingsThemeAction.ShowError(error = error))
        }
    }

    private fun setNewTheme(newTheme: AppTheme, isDark: Boolean) {
        settingsManager.setSelectedTheme(newTheme)
        platform.changeThemeBars(isDark = isDark)
        ThemeManager.setTheme(theme = newTheme)
        updateViewState { state ->
            state.copy(selectedTheme = newTheme)
        }
    }

    override fun obtainEvent(event: SettingsThemeEvent) {
        when(event) {
            is SettingsThemeEvent.OnSelectedNewTheme -> setNewTheme(
                newTheme = event.newTheme,
                isDark = event.isDark
            )
        }
    }
}