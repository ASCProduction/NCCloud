package tk.shkabaj.android.shkabaj.di

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import tk.shkabaj.android.shkabaj.NativeViewsProvider
import tk.shkabaj.android.shkabaj.data.db.AppDatabase
import tk.shkabaj.android.shkabaj.data.local.db.getAppDatabase
import tk.shkabaj.android.shkabaj.player.MPNowPlayingInfoArtworkLoader
import tk.shkabaj.android.shkabaj.utils.IosPlatform
import tk.shkabaj.android.shkabaj.utils.Platform

fun initKoinIos(
    artworkLoader: MPNowPlayingInfoArtworkLoader,
    nativeViewsProvider: NativeViewsProvider
): KoinApplication {
    return initKoin(appModule = module {
        single { artworkLoader }
        single { nativeViewsProvider }
    })
}

actual val platformModule = module {
    single<Platform> { IosPlatform() }
    single<AppDatabase> { getAppDatabase() }
}

@OptIn(BetaInteropApi::class)
fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier? = null): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier)
}

@OptIn(BetaInteropApi::class)
fun Koin.get(objCProtocol: ObjCProtocol, qualifier: Qualifier? = null): Any {
    val kClazz = getOriginalKotlinClass(objCProtocol)!!
    return get(kClazz, qualifier)
}
