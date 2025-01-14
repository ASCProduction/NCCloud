package tk.shkabaj.android.shkabaj.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import tk.shkabaj.android.shkabaj.MainActivity
import tk.shkabaj.android.shkabaj.NCCloudApp

actual val platformType = PlatformType.ANDROID

internal class AndroidPlatform(private val context: Context) : Platform {

    override fun getAppVersion(): String {
        var versionName = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionName
    }

    override fun shareNews(newsTitle: String) {
        sharingInfo(
            text = "$newsTitle - "
        )
    }

    private fun sharingInfo(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/html"
        }
        val chooserIntent = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        NCCloudApp.context.startActivity(chooserIntent)
    }

    override fun clearCache() {
        val cacheDir = NCCloudApp.context.cacheDir
        cacheDir?.let { dir ->
            if(dir.isDirectory) {
                dir.deleteRecursively()
            }
        }
    }

    override fun openUrl(url: String) {
        val context: Context = MainActivity.activityContext
        val customTabsIntentBuilder = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .addDefaultShareMenuItem()

        val customTabsIntent = customTabsIntentBuilder.build()

        val packageName = "com.android.chrome"
        val intent = customTabsIntent.intent
        intent.setPackage(packageName)
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    override fun changeThemeBars(isDark: Boolean) {
        val window = (MainActivity.activityContext as? Activity)?.window
        val statusBarColor = if (isDark) Color(0xFF08232D) else Color(0xFFFFFFFF)
        val navigationBarColor = if (isDark) Color(0xFF043143) else Color(0xFFFFFFFF)
        window?.statusBarColor = statusBarColor.toArgb()
        window?.navigationBarColor = navigationBarColor.toArgb()

        if(window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

}
