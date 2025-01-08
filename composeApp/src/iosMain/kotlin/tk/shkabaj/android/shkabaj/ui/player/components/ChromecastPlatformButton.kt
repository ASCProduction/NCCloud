package tk.shkabaj.android.shkabaj.ui.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import org.koin.compose.koinInject
import tk.shkabaj.android.shkabaj.NativeViewsProvider
import tk.shkabaj.android.shkabaj.extensions.toUIColor

@Composable
actual fun ChromecastPlatformButton(modifier: Modifier, bgColor: Color?) {
    val provider = koinInject<NativeViewsProvider>()
    UIKitView(
        factory = {
            val view = provider.getChromecastButtonView()
            view.backgroundColor = bgColor?.toUIColor()
            return@UIKitView view
        },
        modifier = modifier
    )
}