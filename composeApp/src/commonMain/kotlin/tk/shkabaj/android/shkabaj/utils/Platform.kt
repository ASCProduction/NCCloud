package tk.shkabaj.android.shkabaj.utils

enum class PlatformType {
    ANDROID, IOS
}

expect val platformType: PlatformType

interface Platform {
    fun getAppVersion(): String
    fun shareNews(newsTitle: String)
    fun clearCache()
    fun openUrl(url: String)
    fun changeThemeBars(isDark: Boolean)
}
