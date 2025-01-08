package tk.shkabaj.android.shkabaj.ui.crypto

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.crypto.CryptoViewModel
import tk.shkabaj.android.shkabaj.modules.crypto.model.CryptoAction
import tk.shkabaj.android.shkabaj.modules.crypto.model.CryptoEvent
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.settings.SettingsScreen
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.crypto.component.CryptoList
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class CryptoScreen : AppScreen {

    override val title: AppScreenTitle? = null
    override val toolbarActions: List<ToolbarAction>
        get() = listOf(ToolbarAction.PLAYER, ToolbarAction.SETTINGS)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel = injectScreenModel<CryptoViewModel>()
        val state by viewModel.viewStates().collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = CryptoEvent.OnToolbarEvent(toolbarAction = toolbarAction))
            }
        }


        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when (uiAction) {
                    is CryptoAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = scope
                    )
                    is CryptoAction.NavigateToSettings -> navigator.push(SettingsScreen())
                    else -> {}
                }
            }
        }

        PullToRefreshBox(
            isRefreshing = state.progress,
            onRefresh = { viewModel.obtainEvent(CryptoEvent.OnRefresh) },
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                MainContent(
                    cryptoList = state.list
                )
            }
        }
    }

    @Composable
    private fun MainContent(
        cryptoList: List<CryptoInfo>
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MainBGColor),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (cryptoList.isNotEmpty()) {
                items(
                    cryptoList.chunked(2),
                    key = { cryptoItem -> cryptoItem[0].id!! }
                ) { cryptoItem ->
                    CryptoList(
                        cryptoItem = cryptoItem
                    )
                }
            }
        }
    }
}
