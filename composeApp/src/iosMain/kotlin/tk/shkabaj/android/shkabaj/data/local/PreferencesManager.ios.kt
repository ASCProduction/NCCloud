package tk.shkabaj.android.shkabaj.data.local

import com.russhwolf.settings.Settings

actual class AppPreferencesFactory {

    actual companion object {
        actual fun createDefaultPreferences(): Settings = Settings()
    }

}