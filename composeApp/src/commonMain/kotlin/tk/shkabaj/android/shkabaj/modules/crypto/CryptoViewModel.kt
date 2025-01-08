package tk.shkabaj.android.shkabaj.modules.crypto

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.modules.crypto.model.CryptoAction
import tk.shkabaj.android.shkabaj.modules.crypto.model.CryptoEvent
import tk.shkabaj.android.shkabaj.modules.crypto.model.CryptoUIEvent
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class CryptoViewModel(
    private val apiService: NCCloudApiService
): BaseViewModel<CryptoUIEvent, CryptoAction, CryptoEvent>(
    initialState = CryptoUIEvent()
) {

    init {
        updateViewState { state ->
            state.copy(progress = true)
        }
        loadData()
    }

    private fun loadData() {
        screenModelScope.launchSafe(
            onAction = {
                val cryptos = apiService.getCrypto()
                updateViewState { state ->
                    state.copy(
                        list = cryptos,
                        progress = false
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = CryptoAction.ShowError(error = error))
            updateViewState { state ->
                state.copy(progress = false)
            }
        }
    }

    private fun handleRefreshing() {
        updateViewState { state ->
            state.copy(progress = true)
        }
        loadData()
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.SETTINGS -> sendViewAction(action = CryptoAction.NavigateToSettings)
            else -> {}
        }
    }

    override fun obtainEvent(event: CryptoEvent) {
        when(event) {
            is CryptoEvent.OnRefresh -> handleRefreshing()
            is CryptoEvent.OnToolbarEvent -> handleToolbarAction(toolbarAction = event.toolbarAction)
        }
    }
}