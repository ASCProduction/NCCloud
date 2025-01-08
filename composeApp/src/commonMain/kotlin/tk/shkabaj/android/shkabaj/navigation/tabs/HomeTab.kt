package tk.shkabaj.android.shkabaj.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.navigator.Navigator
import tk.shkabaj.android.shkabaj.ui.main.MainScreen
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.ui.customcomponents.PlatformNavigatorContent

object HomeTab : AppTab {

    override var toolbarManager: ToolbarManager? = null
    override val tabItem: TabItem
        get() = TabItem.HOME

    @Composable
    override fun Content() {
        Navigator(screen = MainScreen()) { navigator ->
            PlatformNavigatorContent(navigator)
            LaunchedEffect(navigator) {
                snapshotFlow { navigator.lastItem }
                    .collect { screen ->
                        toolbarManager?.updateScreen(screen as AppScreen)
                    }
            }
        }
    }

}