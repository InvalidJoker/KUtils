package de.joker.kutils.core.extensions

import dev.fruxz.ascend.tool.time.TimeUnit
import dev.fruxz.ascend.tool.time.calendar.Calendar
import dev.fruxz.ascend.tool.time.clock.TimeDisplay
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.time.Duration

fun <T> Iterable<T>.sumOf(selector: (T) -> Duration): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

val Duration.betterString: String
    get() {
        return TimeDisplay(this).toClockString(TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND)
    }

val String.calendar: Calendar
    get() = calendarFromDateString(this)

fun calendarFromDateString(dateFormat: String, format: Locale = Locale.GERMAN): Calendar {
    val cal: java.util.Calendar = java.util.Calendar.getInstance()
    val sdf = SimpleDateFormat("dd.MM.yyyy", format)
    cal.time = sdf.parse(dateFormat)
    return Calendar.fromLegacy(cal)
}

fun Calendar.formatToDay(locale: Locale): String {
    return SimpleDateFormat.getDateInstance(Calendar.FormatStyle.FULL.ordinal, locale).format(javaDate)
}

fun Instant.toCalendar() = Calendar(this)
