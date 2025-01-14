package tk.shkabaj.android.shkabaj.modules.settings

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction
import tk.shkabaj.android.shkabaj.utils.Platform

data class SettingsState(
    val startTab: TabItem? = null,
    val theme: AppTheme? = null,
)

sealed interface SettingsAction {
    data class ShowError(val error: Throwable): SettingsAction
    data object PopupNavigation: SettingsAction
}

sealed interface SettingsEvent {
    data object OnDefaultScreenChanged: SettingsEvent
    data object OnDefaultThemeChanged: SettingsEvent
    data object ClearCache: SettingsEvent
    data class OnOpenUrl(val url: String): SettingsEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): SettingsEvent
}

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val platform: Platform
) : BaseViewModel<SettingsState, SettingsAction, SettingsEvent>(
    initialState = SettingsState()
) {

    init {
        screenModelScope.launchSafe(
            onAction = {
                val startTab = settingsManager.getStartTab()
                val selectedTheme = settingsManager.getSelectedTheme()
                updateViewState { state ->
                    state.copy(
                        startTab = startTab,
                        theme = selectedTheme
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun clearAppCache() {
        screenModelScope.launch {
            platform.clearCache()
        }
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.BACK_NAVIGATION -> sendViewAction(action = SettingsAction.PopupNavigation)
            else -> {}
        }
    }

    override fun obtainEvent(event: SettingsEvent) {
        when (event) {
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
            SettingsEvent.ClearCache -> clearAppCache()
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