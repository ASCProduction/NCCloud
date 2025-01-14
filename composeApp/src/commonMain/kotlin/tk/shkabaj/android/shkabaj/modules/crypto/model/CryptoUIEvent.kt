package tk.shkabaj.android.shkabaj.modules.crypto.model

import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

data class CryptoUIEvent (
    val list: List<CryptoInfo> = emptyList(),
    val progress: Boolean = false
)

sealed interface CryptoAction {
    data class ShowError(val error: Throwable): CryptoAction
    data object NavigateToSettings: CryptoAction
}

sealed interface CryptoEvent {
    data object OnRefresh: CryptoEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): CryptoEvent
}