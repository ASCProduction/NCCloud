package tk.shkabaj.android.shkabaj.utils

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.contains
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tk.shkabaj.android.shkabaj.NCCloudApp
import tk.shkabaj.android.shkabaj.data.local.PreferencesKey
import tk.shkabaj.android.shkabaj.data.local.PreferencesManager
import tk.shkabaj.android.shkabaj.data.local.db.getAppDatabase
import tk.shkabaj.android.shkabaj.extensions.parseJson
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.migration.model.OldNewsFavouriteModel
import tk.shkabaj.android.shkabaj.migration.model.toDatabaseEntity
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import java.io.FileInputStream
import java.io.IOException

actual class PlatformMigrationHelper: KoinComponent {

    private val context = NCCloudApp.context

    private val newsFileName = "bookmarks"
    private val radioFileName = "favoritesRadios"

    private val weatherPrefsKey = "MotiPreferences"
    private val weatherIndexKey = "reload"
    private val startScreenKey = "START_SCREEN"
    private val isGDPRApplicableKey = "GDPR_APPLICABLE_KEY"
    private val startAppCountKey = "START_APP_COUNT_KEY"

    private val database = getAppDatabase(context)
    private val apiService: NCCloudApiService by inject()
    private val sharedPreferences: PreferencesManager by inject()
    private val countriesManager: CountriesManager by inject()
    private val settingsManager: SettingsManager by inject()
    private val weatherSharedPreferences =
        SharedPreferencesSettings.Factory(context).create(weatherPrefsKey)
    private val defaultSharedPreferences =
        SharedPreferencesSettings.Factory(context).create(context.packageName)

    private val defaultKeys = listOf(
        startScreenKey,
        isGDPRApplicableKey,
        startAppCountKey
    ).filter { defaultSharedPreferences.contains(it) }.toMutableList()

    actual suspend fun migratePreferences(): Boolean {
        if (defaultKeys.isEmpty() && weatherSharedPreferences.contains(weatherIndexKey).not()) {
            println("MigrationHelper migration keys not exist")
            return true
        }

        updateWeatherPref()
        updateStartTabPref()
        updateGDPRApplicable()
        updateStartAppCount()
        return defaultKeys.isEmpty() && weatherSharedPreferences.contains(weatherIndexKey).not()
    }

    actual suspend fun migrateNewsBookmarks(): Boolean {
        val newsInfo = readFromFile(context = context, fileName = newsFileName)
        if (newsInfo.isEmpty()) return true
        val oldNewsInfo: List<OldNewsFavouriteModel> = newsInfo.parseJson()
        oldNewsInfo.forEach { newsItemInfo ->
            database.getNewsDao().insert(newsItemInfo.toDatabaseEntity())
        }
        return true
    }

    private suspend fun updateWeatherPref(){
        val startIndexWeather = weatherSharedPreferences.getIntOrNull(weatherIndexKey)
        startIndexWeather?.let {
            countriesManager.loadCountries().getOrNull(it)?.let {
                countriesManager.setSelectedCountryName(it.name)
            }
            weatherSharedPreferences.remove(weatherIndexKey)
        }
    }

    private fun updateStartTabPref() {
        val startScreenIndex = defaultSharedPreferences.getIntOrNull(startScreenKey)
        startScreenIndex?.let {
            when (it) {
                0 -> settingsManager.setStartTab(TabItem.HOME)
                1 -> settingsManager.setStartTab(TabItem.NEWS)
                2 -> settingsManager.setStartTab(TabItem.WEATHER)
                3 -> settingsManager.setStartTab(TabItem.CRYPTO)
                else -> settingsManager.setStartTab(TabItem.HOME)
            }
            defaultKeys.remove(startScreenKey)
            defaultSharedPreferences.remove(startScreenKey)
        }
    }

    private fun updateGDPRApplicable(){
        val isGDPRApplicable = defaultSharedPreferences.getBooleanOrNull(isGDPRApplicableKey)
        isGDPRApplicable?.let {
            sharedPreferences.putBoolean(PreferencesKey.IS_GDPR_APPLICABLE, it)
            defaultKeys.remove(isGDPRApplicableKey)
            defaultSharedPreferences.remove(isGDPRApplicableKey)
        }
    }

    private fun updateStartAppCount() {
        val startAppCount = defaultSharedPreferences.getIntOrNull(startAppCountKey)
        startAppCount?.let {
            sharedPreferences.putInt(PreferencesKey.GDPR_PRESENT_COUNT, it)
            defaultKeys.remove(startAppCountKey)
            defaultSharedPreferences.remove(startAppCountKey)
        }
    }

    private fun readFromFile(context: Context, fileName: String): String {
        return try {
            val inputStream: FileInputStream = context.openFileInput(fileName)
            val buffer = ByteArray(1024)
            val fileContent = StringBuilder()
            var n: Int
            while (inputStream.read(buffer).also { n = it } != -1) {
                fileContent.append(String(buffer, 0, n))
            }
            fileContent.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}