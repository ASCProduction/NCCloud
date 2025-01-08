package tk.shkabaj.android.shkabaj

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import org.koin.android.ext.android.inject
import tk.shkabaj.android.shkabaj.notification.OneSignalNotificationService
import tk.shkabaj.android.shkabaj.player.NowPlayingManager

class MainActivity : FragmentActivity() {

    private val nowPlayingManager by inject<NowPlayingManager>()
    private val notification by inject<OneSignalNotificationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        activityContext = this
        enableEdgeToEdge()

        val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val statusBarColor = if (isDarkTheme) Color(0xFF08232D) else Color(0xFFFFFFFF)
        val navigationBarColor = if (isDarkTheme) Color(0xFF043143) else Color(0xFFFFFFFF)

        window.statusBarColor = statusBarColor.toArgb()
        window.navigationBarColor = navigationBarColor.toArgb()

        setContent {
            App()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var activityContext: Context
            private set
    }

    override fun onDestroy() {
        super.onDestroy()
        nowPlayingManager.cleanUp()
    }

    override fun onPause() {
        super.onPause()
        notification.changeStateActivity(false)
    }

    override fun onResume() {
        super.onResume()
        notification.changeStateActivity(true)
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}