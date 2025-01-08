package tk.shkabaj.android.shkabaj.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {

    val currentTimestamp: Double
        get() = nowInstant.epochSeconds.toDouble()
    private val nowInstant: Instant
        get() = Clock.System.now()
    private val now: LocalDateTime
        get() = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())

    fun getCurrentYear(): Int = now.year

    fun dateStringFromInstant(instant: Instant): String? {
        try {
            val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val albanianMonths = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            val monthShortName = albanianMonths[date.monthNumber - 1]
            return "${date.dayOfMonth} $monthShortName ${date.year}"
        } catch (e: Exception) {
            return null
        }
    }
    fun dateStringFromTimestamp(timestamp: Double): String? {
        return try {
            dateStringFromInstant(Instant.fromEpochSeconds(timestamp.toLong()))
        } catch (e: Exception) {
            null
        }
    }

    fun formatWeatherDateTime(dt: String?): String {
        if (dt == null) return ""
        val (month, day) = dt.split("/").map { it.toInt() }
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        val date = LocalDate(currentYear, month, day)
        val albanianDaysOfWeek = listOf(
            "Mon", "Tue", "Wen", "Thu",
            "Fri", "Sat", "Sun"
        )
        val albanianMonths = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val dayOfWeek = albanianDaysOfWeek[date.dayOfWeek.ordinal]
        val monthName = albanianMonths[date.monthNumber - 1]
        return "$dayOfWeek, ${date.dayOfMonth} $monthName"
    }

}