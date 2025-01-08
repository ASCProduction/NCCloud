package tk.shkabaj.android.shkabaj.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import tk.shkabaj.android.shkabaj.ui.news.NewsScreen
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.ui.customcomponents.PlatformNavigatorContent

object NewsTab : AppTab {

    override var toolbarManager: ToolbarManager? = null
    override val tabItem: TabItem
        get() = TabItem.NEWS

    @Composable
    override fun Content() {
        Navigator(NewsScreen()) { navigator ->
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