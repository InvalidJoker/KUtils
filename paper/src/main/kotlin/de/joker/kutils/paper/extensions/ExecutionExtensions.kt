package de.joker.kutils.paper.extensions

import org.bukkit.Bukkit


const val nanosPerTick = 25 * 1000L * 1000L // 25ms Reserved for unmeasured tasks
const val estEventsPerTick = 100L
const val nanosPerEvent = nanosPerTick / estEventsPerTick

var enableTimingAnalysis = true


/**
 * Measures execution time and shows if the execution exceeds the reserved
 * execution time defined by the period.
 */
fun <T> timeLimit(period: Long = 1, name: String = "", block: () -> T): T {
    if (!enableTimingAnalysis) {
        return block()
    }
    val limit = (nanosPerEvent * period).coerceAtMost(nanosPerTick)
    val start = System.nanoTime()
    val value = block()
    val measured = System.nanoTime() - start
    if (measured > limit) {
        getPluginLogger().warn("Can't keep up. Time limit breached. $name: $measured ns > $limit ns")
    }
    return value
}

fun <T> timeIt(name: String, block: () -> T): T {
    if (!enableTimingAnalysis) {
        return block()
    }
    val start = System.nanoTime()
    val value = block()
    getPluginLogger().info("$name took ${System.nanoTime() - start}ns")
    return value
}


private var syncAllowed = false

fun <T> requireAsync(block: () -> T): T {
    if (!syncAllowed && Bukkit.isPrimaryThread()) {
        throw IllegalCallerException("Blocking on Server thread detected.")
    }
    return block()
}

fun allowPrimaryBlock() {
    syncAllowed = true
}

fun denyPrimaryBlock() {
    syncAllowed = false
}
