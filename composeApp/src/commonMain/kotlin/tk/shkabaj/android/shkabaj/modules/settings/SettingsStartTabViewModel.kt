package tk.shkabaj.android.shkabaj.modules.settings

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem

data class SettingsStartTabUIState(
    val tabs: List<TabItem> = TabItem.entries,
    val selectedTab: TabItem = TabItem.HOME
)

sealed interface SettingsStartTabAction {
    data class ShowError(val error: Throwable): SettingsStartTabAction
}

sealed interface SettingsStartTabEvent {
    data class OnStartTabSelected(val tabItem: TabItem): SettingsStartTabEvent
    data object OnApplyClicked: SettingsStartTabEvent
}

class SettingsStartTabViewModel(
    private val settingsManager: SettingsManager
): BaseViewModel<SettingsStartTabUIState, SettingsStartTabAction, SettingsStartTabEvent>(
    initialState = SettingsStartTabUIState()
) {

    init {
        screenModelScope.launchSafe(
            onAction = { loadFirstState() },
            onError = ::handleError
        )
    }

    private fun loadFirstState() {
        val selectedScreen = settingsManager.getStartTab()
        updateViewState { state ->
            state.copy(selectedTab = selectedScreen)
        }
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = SettingsStartTabAction.ShowError(error = error))
        }
    }

    private fun updateSelectedScreenState(newScreen: TabItem) {
        if(viewState.selectedTab == newScreen) return
        updateViewState { state ->
            state.copy(selectedTab = newScreen)
        }
    }

    private fun applyChangesOfScreen() {
        viewState.selectedTab.let { tab ->
            settingsManager.setStartTab(tab)
        }
    }

    override fun obtainEvent(event: SettingsStartTabEvent) {
        when(event) {
            is SettingsStartTabEvent.OnStartTabSelected -> updateSelectedScreenState(
                newScreen = event.tabItem
            )
            is SettingsStartTabEvent.OnApplyClicked -> applyChangesOfScreen()
        }
    }
}