package tk.shkabaj.android.shkabaj.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tk.shkabaj.android.shkabaj.data.db.AppDatabase
import tk.shkabaj.android.shkabaj.data.local.db.getAppDatabase
import tk.shkabaj.android.shkabaj.notification.OneSignalNotificationService
import tk.shkabaj.android.shkabaj.player.ChromeCastHelper
import tk.shkabaj.android.shkabaj.player.ChromeCastHelperImpl
import tk.shkabaj.android.shkabaj.utils.AndroidPlatform
import tk.shkabaj.android.shkabaj.utils.Platform

fun initAndroidCoin(context: Context) {
    initKoin(appDeclaration = {
        androidContext(context)
    })
}

actual val platformModule = module {
    single<Platform> { AndroidPlatform(androidContext()) }
    single<AppDatabase> { getAppDatabase(get()) }
    single<OneSignalNotificationService> { OneSignalNotificationService(androidContext(), get()) }
    single<ChromeCastHelper>(createdAtStart = true) { ChromeCastHelperImpl() }
}