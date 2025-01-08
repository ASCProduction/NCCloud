package tk.shkabaj.android.shkabaj.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
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

    override fun sendSupportEmail() {
        val subject = "Shkabaj App ${getAppVersion()} – Android"
        val recipient = Constants.SHARE_EMAIL_ADDRESS

        val intent = Intent(Intent.ACTION_SEND).apply {
            setType("message/rfc822")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun shareApp() {
        val link = Constants.SHARE_APP_LINK
        sharingInfo(
            text = "Shkarkoni aplikacionin Shkabaj për Android falas në Google Play Store ose përmes <a href=\"$link\">$link</a>"
        )
    }

    override fun shareNews(newsTitle: String) {
        val link = Constants.SHARE_APP_LINK
        sharingInfo(
            text = "Po i shikoj lajmet $newsTitle në aplikacionin Shkabaj për Android \"$link\">$link"
        )
    }

    override fun shareRadio(radioName: String) {
        val link = Constants.SHARE_APP_LINK
        sharingInfo(
            text = "Jam duke dëgjuar $radioName në aplikacionin Shkabaj për Android \"$link\">$link"
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

//        if (isDark) {
//            customTabsIntentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.darkToolbarColor))
//            customTabsIntentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.darkSecondaryColor))
//        } else {
//            customTabsIntentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.lightToolbarColor))
//            customTabsIntentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.lightSecondaryColor))
//        }

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
