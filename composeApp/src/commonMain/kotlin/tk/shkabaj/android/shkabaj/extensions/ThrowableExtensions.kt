package tk.shkabaj.android.shkabaj.extensions

import okio.IOException
import org.jetbrains.compose.resources.getString
import nccloud.composeapp.generated.resources.InternetConnectionErrorMessage
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.UnknownErrorMessage

suspend fun Throwable.getErrorMessage(): String {
    return try {
        when (this) {
            is IOException -> getString(Res.string.InternetConnectionErrorMessage)
            else -> getString(resource = Res.string.UnknownErrorMessage) + message
        }
    } catch(e: Exception) {
        ""
    }
}