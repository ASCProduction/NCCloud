package tk.shkabaj.android.shkabaj.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import tk.shkabaj.android.shkabaj.data.local.PreferencesManager
import tk.shkabaj.android.shkabaj.di.qualifiers.AppHttpClient
import tk.shkabaj.android.shkabaj.di.qualifiers.CommonHttpClient
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.managers.bookmarks.NewsBookmarksManager
import tk.shkabaj.android.shkabaj.modules.mainscreen.MainScreenViewModel
import tk.shkabaj.android.shkabaj.modules.crypto.CryptoViewModel
import tk.shkabaj.android.shkabaj.modules.main.MainViewModel
import tk.shkabaj.android.shkabaj.modules.news.NewsParentViewModel
import tk.shkabaj.android.shkabaj.modules.news.NewsBookmarksViewModel
import tk.shkabaj.android.shkabaj.modules.news.NewsMainViewModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsStartTabViewModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsThemeViewModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsViewModel
import tk.shkabaj.android.shkabaj.managers.SearchBarManager
import tk.shkabaj.android.shkabaj.managers.UpdatesManager
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarManager
import tk.shkabaj.android.shkabaj.modules.weather.WeatherViewModel
import tk.shkabaj.android.shkabaj.network.NetworkClient
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.notifications.NavigationHelper
import tk.shkabaj.android.shkabaj.notifications.NotificationClickHandler
import tk.shkabaj.android.shkabaj.player.AudioPlayer
import tk.shkabaj.android.shkabaj.player.NowPlayingManager

fun initKoin(appDeclaration: KoinAppDeclaration? = null,
             appModule: Module? = null): KoinApplication {
    return startKoin {
        appDeclaration?.invoke(this)
        modules(
            listOfNotNull(appModule, mainModule, networkModule, preferencesModule, platformModule)
        )
    }
}

expect val platformModule: Module

val networkModule = module {
    single<NetworkClient>(qualifier = AppHttpClient) { NetworkClient.getInstance(withBaseUrl = true) }
    single<NetworkClient>(qualifier = CommonHttpClient) { NetworkClient.getInstance(withBaseUrl = false) }
    single { NCCloudApiService(get(AppHttpClient), get(CommonHttpClient)) }
}


val mainModule = module {
    single { NavigationHelper() }
    single { NotificationClickHandler(get()) }
    single { AudioPlayer() }
    single { NowPlayingManager(get(), get(), get()) }
    single { NewsBookmarksManager(get()) }
    singleOf(::CountriesManager)
    singleOf(::SettingsManager)
    singleOf(::ToolbarManager)
    singleOf(::SearchBarManager)
    singleOf(::UpdatesManager)
    factoryOf(::MainScreenViewModel)
    factoryOf(::CryptoViewModel)
    factoryOf(::MainViewModel)
    factoryOf(::WeatherViewModel)
    factoryOf(::SettingsViewModel)
    factoryOf(::NewsParentViewModel)
    factoryOf(::NewsBookmarksViewModel)
    factoryOf(::NewsMainViewModel)
    factoryOf(::SettingsStartTabViewModel)
    factoryOf(::SettingsThemeViewModel)
}

val preferencesModule = module {
    single { PreferencesManager.buildDefault() }
}
