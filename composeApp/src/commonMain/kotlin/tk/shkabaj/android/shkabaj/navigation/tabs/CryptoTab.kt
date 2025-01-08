package tk.shkabaj.android.shkabaj.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.navigator.Navigator
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.ui.crypto.CryptoScreen
import tk.shkabaj.android.shkabaj.ui.customcomponents.PlatformNavigatorContent
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager

object CryptoTab : AppTab {
    override val tabItem: TabItem
        get() = TabItem.CRYPTO
    override var toolbarManager: ToolbarManager? = null

    @Composable
    override fun Content() {
        Navigator(CryptoScreen()) { navigator ->
            PlatformNavigatorContent(navigator)
            LaunchedEffect(navigator) {
                snapshotFlow { navigator.lastItem }
                    .collect { screen ->
                        WeatherTab.toolbarManager?.updateScreen(screen as AppScreen)
                    }
            }
        }
    }

}