package tk.shkabaj.android.shkabaj.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.AlwaysBright
import nccloud.composeapp.generated.resources.AlwaysDark
import nccloud.composeapp.generated.resources.AsSystem
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_dark_theme_32
import nccloud.composeapp.generated.resources.ic_light_theme_32
import nccloud.composeapp.generated.resources.ic_system_theme_32

enum class AppTheme {
    Light,
    Dark,
    System;

    val title
        @Composable
        get() = when (this) {
            Light -> stringResource(Res.string.AlwaysBright)
            Dark -> stringResource(Res.string.AlwaysDark)
            System -> stringResource(Res.string.AsSystem)
        }

    val icon
        get() = when (this) {
            Light -> Res.drawable.ic_light_theme_32
            Dark -> Res.drawable.ic_dark_theme_32
            System -> Res.drawable.ic_system_theme_32
        }

    val isDark: Boolean
        @Composable
        get() = when (this) {
            Light -> false
            Dark -> true
            System -> isSystemInDarkTheme()
        }
}