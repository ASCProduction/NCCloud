package tk.shkabaj.android.shkabaj.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.No
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.Yes
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.main.MainAction
import tk.shkabaj.android.shkabaj.modules.main.MainViewModel
import tk.shkabaj.android.shkabaj.navigation.tabs.CryptoTab
import tk.shkabaj.android.shkabaj.navigation.tabs.HomeTab
import tk.shkabaj.android.shkabaj.navigation.tabs.NewsTab
import tk.shkabaj.android.shkabaj.navigation.tabs.WeatherTab
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.OpacityBGColor
import tk.shkabaj.android.shkabaj.ui.theme.WhiteColor
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarLayout
import tk.shkabaj.android.shkabaj.utils.Constants

class MainScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<MainViewModel>()
        val mainState = viewModel.viewStates().collectAsState()
        val uriHandler = LocalUriHandler.current

        val pushDialogAction = remember {
            mutableStateOf<NavigationHelper.Action.ShowPushDialog?>(null)
        }
        var tabNavigator: TabNavigator? = null

        val hidingBottomBar = mainState.value.isHideBottomBar
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val tabs = listOf(HomeTab, NewsTab, WeatherTab, CryptoTab)
        val initialTab = tabs.find { it.tabItem == mainState.value.initialTab } ?: HomeTab
        tabs.forEach { it.toolbarManager = mainState.value.toolbarManager }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is MainAction.ShowError -> {
                        showSnackBar(
                            message = uiAction.error.getErrorMessage(),
                            snackBarHostState = snackBarHostState,
                            coroutine = coroutineScope
                        )
                    }
                    is MainAction.HandleNavigationAction -> {
                        when(val navAction = uiAction.action) {
                            is NavigationHelper.Action.ShowPushDialog -> {
                                pushDialogAction.value = navAction
                            }
                            is NavigationHelper.Action.OpenLink -> {
                                if (navAction.insideApp) {
                                    viewModel.openUrl(navAction.link)
                                } else {
                                    uriHandler.openUri(navAction.link)
                                }
                            }
                            is NavigationHelper.Action.OpenTab -> {
                                tabs.find { it.tabItem == navAction.tab }?.let {
                                    tabNavigator?.current = it
                                }
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }

        if (pushDialogAction.value != null) {
            PushDialog(
                onDismissRequest = {
                    pushDialogAction.value = null
                },
                onConfirmation = {
                    pushDialogAction.value?.handler?.invoke()
                    pushDialogAction.value = null
                },
                dialogText = pushDialogAction.value?.title ?: ""
            )
        }

        TabNavigator(tab = initialTab) {
            tabNavigator = LocalTabNavigator.current
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    modifier = Modifier.systemBarsPadding(),
                    topBar = {
                        ToolbarLayout(
                            toolbarManager = mainState.value.toolbarManager
                        )
                    },
                    bottomBar = {
                        if (!hidingBottomBar) {
                            BottomAppBar(
                                backgroundColor = WhiteColor,
                                content = {
                                    tabs.forEach {
                                        TabNavigationItem(
                                            tab = it,
                                            currentTabItem = it.tabItem,
                                            showUpdateIndicator = mainState.value.tabsWithUpdates.contains(it.tabItem)
                                        )
                                    }
                                }
                            )
                        }
                    },
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        CurrentScreen()
                    }
                }
                if (mainState.value.isSearchBarFocused && mainState.value.isScreenWithSearchBar) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(
                                top = WindowInsets
                                    .statusBars
                                    .asPaddingValues()
                                    .calculateTopPadding() + Constants.TOOLBAR_HEIGHT.dp + Constants.SEARCHBAR_HEIGHT.dp
                            )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(color = OpacityBGColor)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PushDialog(
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit,
        dialogText: String
    ) {
        AlertDialog(
            text = { Text(text = dialogText) },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmation() }
                ) {
                    Text(stringResource(Res.string.Yes), color = AccentColor)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(stringResource(Res.string.No), color = AccentColor)
                }
            }
        )
    }

}
