package de.joker.core.extensions


import dev.fruxz.ascend.tool.time.TimeUnit
import dev.fruxz.ascend.tool.time.calendar.Calendar
import dev.fruxz.ascend.tool.time.clock.TimeDisplay
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
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

fun calendarFromDateString(dateFormat: String): Calendar {
    val cal: java.util.Calendar = java.util.Calendar.getInstance()
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
    cal.time = sdf.parse(dateFormat) // all done
    return Calendar.fromLegacy(cal)
}

fun Calendar.formatToDay(locale: Locale): String {
    return SimpleDateFormat.getDateInstance(Calendar.FormatStyle.FULL.ordinal, locale).format(javaDate)
}

fun isWeekend(): Boolean {
    val now = LocalDateTime.now()
    val dayOfWeek = now.dayOfWeek
    val hour = now.hour

    return when (dayOfWeek) {
        DayOfWeek.FRIDAY -> hour >= 18
        DayOfWeek.SATURDAY -> true
        DayOfWeek.SUNDAY -> hour < 22
        else -> false
    }
}


fun parseDurationStringToEpoch(durationString: String): Long? {
    if (durationString.isEmpty()) return null

    val regex = "(\\d+)([smhdw])".toRegex(RegexOption.IGNORE_CASE)
    val match = regex.matchEntire(durationString)

    if (match != null) {
        val (valueStr, unit) = match.destructured
        val value = valueStr.toLongOrNull() ?: return null

        if (value <= 0) return null

        val durationMillis = when (unit.lowercase()) {
            "s" -> java.util.concurrent.TimeUnit.SECONDS.toMillis(value)
            "m" -> java.util.concurrent.TimeUnit.MINUTES.toMillis(value)
            "h" -> java.util.concurrent.TimeUnit.HOURS.toMillis(value)
            "d" -> java.util.concurrent.TimeUnit.DAYS.toMillis(value)
            "w" -> java.util.concurrent.TimeUnit.DAYS.toMillis(value * 7)
            else -> return null
        }

        return System.currentTimeMillis() + durationMillis
    }

    return null
}

fun Instant.toCalendar() =
    Calendar(GregorianCalendar.from(ZonedDateTime.from(this.atZone(ZoneId.systemDefault()))))
