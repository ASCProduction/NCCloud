package tk.shkabaj.android.shkabaj.ui.settings

import org.jetbrains.compose.resources.DrawableResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_settings_facebook
import nccloud.composeapp.generated.resources.ic_settings_facebook_dark
import nccloud.composeapp.generated.resources.ic_settings_home
import nccloud.composeapp.generated.resources.ic_settings_home_dark
import nccloud.composeapp.generated.resources.ic_settings_mail
import nccloud.composeapp.generated.resources.ic_settings_mail_dark
import nccloud.composeapp.generated.resources.ic_settings_privace_policy
import nccloud.composeapp.generated.resources.ic_settings_privace_policy_dark
import nccloud.composeapp.generated.resources.ic_settings_reference
import nccloud.composeapp.generated.resources.ic_settings_reference_dark
import nccloud.composeapp.generated.resources.ic_settings_send
import nccloud.composeapp.generated.resources.ic_settings_send_dark
import nccloud.composeapp.generated.resources.ic_settings_theme
import nccloud.composeapp.generated.resources.ic_settings_theme_dark
import nccloud.composeapp.generated.resources.ic_settings_tos
import nccloud.composeapp.generated.resources.ic_settings_tos_dark
import nccloud.composeapp.generated.resources.ic_settings_x
import nccloud.composeapp.generated.resources.ic_settings_x_dark
import nccloud.composeapp.generated.resources.ic_settings_weather
import nccloud.composeapp.generated.resources.ic_settings_weather_dark

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

    data class Weather(
        override val title: String,
        override val subtitle: String?,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_weather,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_weather_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title, subtitle)

    data class Theme(
        override val title: String,
        override val subtitle: String?,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_theme,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_theme_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title, subtitle)

    data class ShareApp(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_send,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_send_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class Mail(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_mail,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_mail_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class Facebook(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_facebook,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_facebook_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class Twitter(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_x,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_x_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class AboutUs(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_reference,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_reference_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class PrivacyPolicy(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_privace_policy,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_privace_policy_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)

    data class TermsOfUse(
        override val title: String,
        override val iconResourceLight: DrawableResource = Res.drawable.ic_settings_tos,
        override val iconResourceDark: DrawableResource = Res.drawable.ic_settings_tos_dark
    ) : SettingEntity(iconResourceLight, iconResourceDark, title)
}