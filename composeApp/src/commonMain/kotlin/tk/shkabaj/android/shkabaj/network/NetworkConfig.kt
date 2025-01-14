package tk.shkabaj.android.shkabaj.network

import tk.shkabaj.android.shkabaj.utils.PlatformType
import tk.shkabaj.android.shkabaj.utils.platformType

object NetworkConfig {

    const val BASE_PORT = 443
    const val BASE_URL = "https://www.shkabaj.net/"
    const val NEWS_IMAGE_BASE_URL = "https://www.shkabaj.net/news/updates/"

    val WEATHER_COUNTRIES_LIST_PATH = when(platformType) {
        PlatformType.ANDROID -> "moti/city-list-android.json"
        PlatformType.IOS -> "moti/city-list-ios.json"
    }

    const val WEATHER_FORECAST_PATH = "moti/wwo.php"

    const val NEWS_PATH = "news/updates/shkabaj.xml"
}