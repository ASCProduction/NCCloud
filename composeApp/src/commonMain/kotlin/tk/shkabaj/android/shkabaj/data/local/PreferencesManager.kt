package tk.shkabaj.android.shkabaj.data.local

import com.russhwolf.settings.Settings

enum class PreferencesKey(val value: String) {
    WEATHER_COUNTRY(value = "weather_country"),
    START_TAB(value = "start_tab"),
    APP_THEME(value = "app_theme"),

    IS_GDPR_APPLICABLE(value = "is_gdpr_applicable"),
    CONSENT_STRING(value = "consent_string"),
    GDPR_PRESENT_COUNT(value = "gdpr_present_count"),

    IS_MIGRATED_TO_V7(value = "is_migrated_to_v7")
}

expect class AppPreferencesFactory {

    companion object {
        fun createDefaultPreferences(): Settings
    }

}

class PreferencesManager(private val settings: Settings) {

    companion object {
        fun buildDefault(): PreferencesManager = PreferencesManager(
            settings = AppPreferencesFactory.createDefaultPreferences()
        )
    }

    fun getStringOrNull(key: PreferencesKey): String? {
        return settings.getStringOrNull(key.value)
    }

    fun getString(key: PreferencesKey, defaultValue: String): String {
        return settings.getString(key.value, defaultValue)
    }

    fun putString(key: PreferencesKey, value: String) {
        settings.putString(key.value, value)
    }

    fun getBoolean(key: PreferencesKey, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key.value, defaultValue)
    }

    fun putBoolean(key: PreferencesKey, value: Boolean) {
        settings.putBoolean(key.value, value)
    }

    fun getLong(key: PreferencesKey, defaultValue: Long): Long {
        return settings.getLong(key.value, defaultValue)
    }

    fun putLong(key: PreferencesKey, value: Long) {
        settings.putLong(key.value, value)
    }

    fun getInt(key: PreferencesKey, defaultValue: Int): Int {
        return settings.getInt(key.value, defaultValue)
    }

    fun putInt(key: PreferencesKey, value: Int) {
        settings.putInt(key.value, value)
    }

    fun removeValue(key: PreferencesKey) {
        settings.remove(key.value)
    }

    fun clear() {
        settings.clear()
    }

}