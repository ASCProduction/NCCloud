package tk.shkabaj.android.shkabaj.extensions

fun String.replaceDays(): String {
    val daysOfWeek = mapOf(
        "Monday" to "E hënë",
        "Tuesday" to "E martë",
        "Wednesday" to "E mërkurë",
        "Thursday" to "E enjte",
        "Friday" to "E premte",
        "Saturday" to "E shtunë",
        "Sunday" to "E diel",
        "Mon" to "Hën",
        "Tue" to "Mar",
        "Wed" to "Mer",
        "Thu" to "Enj",
        "Fri" to "Pre",
        "Sat" to "Sht",
        "Sun" to "Die"
    )
    var result = this
    for ((englishDay, albanianDay) in daysOfWeek) {
        result = result.replace(englishDay, albanianDay, ignoreCase = true)
    }
    return result
}

fun String.replaceMonths(): String {
    val months = mapOf(
        "January" to "Janar",
        "February" to "Shkurt",
        "March" to "Mars",
        "April" to "Prill",
        "May" to "Maj",
        "June" to "Qershor",
        "July" to "Korrik",
        "August" to "Gusht",
        "September" to "Shtator",
        "October" to "Tetor",
        "November" to "Nëntor",
        "December" to "Dhjetor",
        "Jan" to "Jan",
        "Feb" to "Shk",
        "Mar" to "Mar",
        "Apr" to "Pri",
        "May" to "Maj",
        "Jun" to "Qer",
        "Jul" to "Kor",
        "Aug" to "Gus",
        "Sep" to "Sht",
        "Oct" to "Tet",
        "Nov" to "Nën",
        "Dec" to "Dhj"
    )
    var result = this
    for ((englishMonth, albanianMonth) in months) {
        result = result.replace(englishMonth, albanianMonth, ignoreCase = true)
    }
    return result
}