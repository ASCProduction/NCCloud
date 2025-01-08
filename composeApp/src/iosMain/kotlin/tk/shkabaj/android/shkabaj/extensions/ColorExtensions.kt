package tk.shkabaj.android.shkabaj.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import platform.UIKit.UIColor

fun Color.toUIColor(): UIColor {
    val argb = this.toArgb()

    val blue = argb and 0xff;
    val green = argb shr 8 and 0xff;
    val red = argb shr 16 and 0xff;
    val alpha = argb shr 24 and 0xff;

    return UIColor(
        red = red /255.0,
        green = green / 255.0,
        blue = blue / 255.0,
        alpha = alpha / 255.0
    )
}