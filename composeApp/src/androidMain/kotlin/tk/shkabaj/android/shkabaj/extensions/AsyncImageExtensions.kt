package tk.shkabaj.android.shkabaj.extensions

import coil3.request.ImageRequest
import coil3.request.allowHardware

actual fun ImageRequest.Builder.androidAllowHardware(allow: Boolean): ImageRequest.Builder = allowHardware(allow)