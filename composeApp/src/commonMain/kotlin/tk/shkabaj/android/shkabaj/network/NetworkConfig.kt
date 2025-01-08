package tk.shkabaj.android.shkabaj.network

import tk.shkabaj.android.shkabaj.utils.PlatformType
import tk.shkabaj.android.shkabaj.utils.platformType

object NetworkConfig {

    const val BASE_PORT = 443
    const val BASE_URL = "https://www.shkabaj.net/"
    const val NEWS_IMAGE_BASE_URL = "https://www.shkabaj.net/news/updates/"
    const val API_KEY_V3 = "AIzaSyDXIf-xkam266LGbIsyVRNblHVWZ4Cyc6M"
    const val RADIO_LISTEN_API_KEY = "123324xcvdsfsdfsdf"

    val WEATHER_COUNTRIES_LIST_PATH = when(platformType) {
        PlatformType.ANDROID -> "moti/city-list-android.json"
        PlatformType.IOS -> "moti/city-list-ios.json"
    }

    const val WEATHER_FORECAST_PATH = "moti/wwo.php"

    val VIDEOS_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/common/video.json"
        PlatformType.IOS -> "mobi/common/video-ios.json"
    }

    val BALLINA_TV_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/common/tv-android.json"
        PlatformType.IOS -> "mobi/common/tv-ios.json"
    }

    const val BALLINA_LINKS_PATH = "mobi/common/ballina-lidhje.json"
    const val NEWS_PATH = "news/updates/shkabaj.xml"

    val RSS_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/common/android-rss.json"
        PlatformType.IOS -> "mobi/common/ios-rss.json"
    }

    const val NEWS_MEDIA_PATH = "mobi/common/lajme-media.json"
    const val NEWS_PODCASTS_PATH = "mobi/common/audiolajme.txt"

    val RADIO_SCREEN_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/android/radio/stats/radios.php"
        PlatformType.IOS -> "mobi/ios/radio/stats/radios.php"
    }

    val RADIO_GROUPS_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/common/radio_groups_android.json"
        PlatformType.IOS -> "mobi/common/radio_groups_ios.json"
    }

    val LISTEN_RADIO_PATH = when(platformType) {
        PlatformType.ANDROID -> "mobi/android/radio/stats/listening.php"
        PlatformType.IOS -> "mobi/ios/radio/stats/listening.php"
    }

    const val VIDEO_CHANNELS_URL = "mobi/common/video.json"

    const val VIDEO_YOUTUBE_URL = "https://www.youtube.com/watch?v="

    fun buildYoutubeVideoLink(videoId: String): String {
        return "${VIDEO_YOUTUBE_URL}${videoId}"
    }

}