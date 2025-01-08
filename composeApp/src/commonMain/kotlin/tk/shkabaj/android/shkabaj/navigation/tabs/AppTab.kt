package tk.shkabaj.android.shkabaj.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import nccloud.composeapp.generated.resources.HomeTab
import nccloud.composeapp.generated.resources.NewsTab
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.WeatherTab
import nccloud.composeapp.generated.resources.CryptoTab
import nccloud.composeapp.generated.resources.ic_navigation_cloud
import nccloud.composeapp.generated.resources.ic_navigation_home
import nccloud.composeapp.generated.resources.ic_navigation_newsmode
import nccloud.composeapp.generated.resources.ic_navigation_crypto
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager

enum class TabItem(val index: UShort) {
    HOME(0u),
    NEWS(1u),
    WEATHER(2u),
    CRYPTO(3u);

    val title: String
        @Composable
        get() = when(this) {
            HOME -> stringResource(Res.string.HomeTab)
            NEWS -> stringResource(Res.string.NewsTab)
            WEATHER -> stringResource(Res.string.WeatherTab)
            CRYPTO -> stringResource(Res.string.CryptoTab)
        }

    val icon: ImageVector
        @Composable
        get() = when(this) {
            HOME -> vectorResource(resource = Res.drawable.ic_navigation_home)
            NEWS -> vectorResource(resource = Res.drawable.ic_navigation_newsmode)
            WEATHER -> vectorResource(resource = Res.drawable.ic_navigation_cloud)
            CRYPTO -> vectorResource(resource = Res.drawable.ic_navigation_crypto)
        }
}

interface AppTab: Tab {

    val tabItem: TabItem
    var toolbarManager: ToolbarManager?

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = tabItem.icon)
            val title = tabItem.title
            return remember {
                TabOptions(index = tabItem.index, title = title, icon = icon)
            }
        }

}