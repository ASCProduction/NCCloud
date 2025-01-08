package tk.shkabaj.android.shkabaj.managers

import tk.shkabaj.android.shkabaj.data.local.PreferencesKey
import tk.shkabaj.android.shkabaj.data.local.PreferencesManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme

class SettingsManager(
    private val preferences: PreferencesManager
) {

    fun setStartTab(tab: TabItem) {
        preferences.putString(PreferencesKey.START_TAB, tab.name)
    }

    fun getStartTab(): TabItem {
        val stored = preferences.getStringOrNull(PreferencesKey.START_TAB)
        return stored?.let { TabItem.entries.find { it.name == stored } } ?: TabItem.HOME
    }

    fun setSelectedTheme(theme: AppTheme) {
        preferences.putString(PreferencesKey.APP_THEME, theme.name)
    }

    fun getSelectedTheme(): AppTheme {
        val stored = preferences.getStringOrNull(PreferencesKey.APP_THEME)
        return stored?.let { AppTheme.entries.find { it.name == stored } } ?: AppTheme.System
    }

}