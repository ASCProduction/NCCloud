package tk.shkabaj.android.shkabaj.extensions

fun Long.asFormatPodcastTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    val minutesString = if (minutes < 10) "0$minutes" else "$minutes"
    val secondsString = if (seconds < 10) "0$seconds" else "$seconds"

    return "$minutesString:$secondsString"
}

fun Int.formatListeningCount(): String {
    if (this < 1000) return this.toString()

    val suffixes = listOf("K", "M", "B")
    val divisor = 1000.0
    val numberDouble = this.toDouble()

    var num = numberDouble
    var suffix = ""
    var i = 0

    while (i < suffixes.size && num >= divisor) {
        num /= divisor
        suffix = suffixes[i]
        i++
    }
    val formattedNumber = num.formatToTwoDecimalPlaces()
    return "$formattedNumber $suffix"
}

fun Double.formatToTwoDecimalPlaces(): String {
    val integerPart = this.toInt()
    val decimalPart = ((this - integerPart) * 100).toInt()

    return if (decimalPart == 0) {
        integerPart.toString()
    } else {
        "$integerPart.${decimalPart.toString().padStart(2, '0')}"
    }
}