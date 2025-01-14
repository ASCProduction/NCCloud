package tk.shkabaj.android.shkabaj.modules.news

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.navigation.NavigationHelper
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

data class NewsParentUIModel(
    val progress: Boolean
)

sealed interface NewsParentAction {
    data object OpenBottomSheetFilter: NewsParentAction
    data object NavigateToFavourite: NewsParentAction
    data object NavigateToAudio: NewsParentAction
    data class SwitchTab(val index: Int): NewsParentAction
}

sealed interface NewsParentEvent {
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): NewsParentEvent
}

class NewsParentViewModel(
    private val navigationHelper: NavigationHelper
): BaseViewModel<NewsParentUIModel, NewsParentAction, NewsParentEvent>(
    initialState = NewsParentUIModel(progress = false)
) {

    init {
        launchNavigationHelperCollecting()
    }

    private fun launchNavigationHelperCollecting() {
        screenModelScope.launch {
            navigationHelper.state.collect { event ->
                when(event) {
                    is NavigationHelper.Action.OpenNewsTab -> {
                        screenModelScope.launch {
                            sendViewAction(action = NewsParentAction.SwitchTab(event.tabIndex))
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun openPlayingRadio() {
        screenModelScope.launch {
            sendViewAction(action = NewsParentAction.NavigateToAudio)
        }
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.FAVOURITE -> sendViewAction(action = NewsParentAction.NavigateToFavourite)
            ToolbarAction.FILTER -> sendViewAction(action = NewsParentAction.OpenBottomSheetFilter)
            ToolbarAction.PLAYER -> openPlayingRadio()
            else -> {}
        }
    }

    override fun obtainEvent(event: NewsParentEvent) {
        when(event) {
            is NewsParentEvent.OnToolbarEvent -> handleToolbarAction(toolbarAction = event.toolbarAction)
        }
    }
}