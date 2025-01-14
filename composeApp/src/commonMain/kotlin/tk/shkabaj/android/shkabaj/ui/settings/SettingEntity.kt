package tk.shkabaj.android.shkabaj.ui.settings

import org.jetbrains.compose.resources.DrawableResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_settings_home
import nccloud.composeapp.generated.resources.ic_settings_home_dark
import nccloud.composeapp.generated.resources.ic_settings_theme
import nccloud.composeapp.generated.resources.ic_settings_theme_dark

sealed class SettingEntity(
    open val iconResourceLight: DrawableResource,
    open val iconResourceDark: DrawableResource,
    open val title: String,
    open val subtitle: String? = null
) {
    data class Home(
        override val title: String,
        override val subtitle: String?,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_home,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_home_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title, subtitle)

    data class Theme(
        override val title: String,
        override val subtitle: String?,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_theme,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_theme_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title, subtitle)
}