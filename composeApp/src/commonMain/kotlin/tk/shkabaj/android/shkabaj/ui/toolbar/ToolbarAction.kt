package tk.shkabaj.android.shkabaj.ui.toolbar

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_back_navigation
import nccloud.composeapp.generated.resources.ic_radio_translate
import nccloud.composeapp.generated.resources.ic_settings
import nccloud.composeapp.generated.resources.ic_share_podcast
import nccloud.composeapp.generated.resources.ic_toolbar_favourite
import nccloud.composeapp.generated.resources.ic_toolbar_filter
import nccloud.composeapp.generated.resources.ic_placeholder
import nccloud.composeapp.generated.resources.ic_toolbar_more

enum class ToolbarAction {
    BACK_NAVIGATION,
    SETTINGS,
    FAVOURITE,
    FILTER,
    SHARE_PODCAST,
    CHROMECAST,
    PLAYER,
    MORE_ACTION;

    val icon: DrawableResource
        @Composable
        get() = when(this) {
            SETTINGS -> Res.drawable.ic_settings
            BACK_NAVIGATION -> Res.drawable.ic_back_navigation
            FAVOURITE -> Res.drawable.ic_toolbar_favourite
            FILTER -> Res.drawable.ic_toolbar_filter
            SHARE_PODCAST -> Res.drawable.ic_share_podcast
            CHROMECAST -> Res.drawable.ic_radio_translate
            PLAYER -> Res.drawable.ic_placeholder
            MORE_ACTION -> Res.drawable.ic_toolbar_more
        }
}