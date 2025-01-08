package tk.shkabaj.android.shkabaj.ui.customcomponents

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
actual fun PlatformNavigatorContent(navigator: Navigator) {
    SlideTransition(navigator)
}