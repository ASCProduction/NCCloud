package tk.shkabaj.android.shkabaj.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun String.parseDateStringToTimestamp(): Double? {
    return try {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val date = formatter.parse(this)
        date?.time?.toDouble()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}