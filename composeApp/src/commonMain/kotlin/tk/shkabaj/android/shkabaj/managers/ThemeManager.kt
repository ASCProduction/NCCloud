package tk.shkabaj.android.shkabaj.managers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme
import tk.shkabaj.android.shkabaj.utils.Platform

object ThemeManager: KoinComponent {

    private val settingsManager by inject<SettingsManager>()
    private val platform by inject<Platform>()

    private var currentTheme = mutableStateOf(AppTheme.System)

    init {
        setTheme(settingsManager.getSelectedTheme())
    }

    fun setTheme(theme: AppTheme) {
        currentTheme.value = theme
        when (theme) {
            AppTheme.Dark -> platform.changeThemeBars(isDark = true)
            AppTheme.Light -> platform.changeThemeBars(isDark = false)
            AppTheme.System -> {}
        }
    }

    @Composable
    fun isDarkThemeEnabled(): Boolean {
        return currentTheme.value.isDark
    }

}