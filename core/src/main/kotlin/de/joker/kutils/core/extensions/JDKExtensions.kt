package de.joker.kutils.core.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.iterator
import kotlin.math.pow

fun <T : Any> T.getLogger(): Logger {
    return LoggerFactory.getLogger(this::class.java)
}

val <T : Any> T.logger: Logger
    get() = getLogger()

fun <T : Any> T.nullIf(condition: (T) -> Boolean): T? {
    return if (condition(this)) null else this
}

fun Collection<String>.filterNotBlank(): List<String> {
    return this.filter { it.isNotBlank() }
}

val Int.to3Digits: String
    get() = "%03d".format(this)

fun Int.toDigits(length: Int): String {
    return "%0${length}d".format(this)
}

fun Int.toDigitsReversed(length: Int): String {
    val max = 10.0.pow(length.toDouble()).toInt() - 1
    return "%0${length}d".format(max - this)
}

val OptionalInt.to3Digits: String
    get() = if (this.isPresent) {
        "%03d".format(this.getAsInt())
    } else {
        "%03d".format(0)
    }

val OptionalInt.to3DigitsReversed: String
    get() = if (this.isPresent) {
        "%03d".format(999 - this.getAsInt())
    } else {
        "%03d".format(999)
    }

fun Int.toFixedString(digits: Int = 3): String {
    return this.toString().padStart(digits, '0')
}

fun String.titlecase(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
}


fun <K> Map<K, Double>.chooseByProbability(): K? {
    val sum = this.values.sum()
    var random = Math.random() * sum
    for ((key, value) in this) {
        random -= value
        if (random <= 0) {
            return key
        }
    }
    return null
}

fun <K> Map<K, Double>.chooseByProbability(amount: Int): List<K> {
    val result = mutableListOf<K>()
    for (i in 0 until amount) {
        val chosen = this.chooseByProbability()
        if (chosen != null) {
            result.add(chosen)
        }
    }
    return result
}


fun <T : Any> T.getThisLogger(): Logger {
    return LoggerFactory.getLogger(this::class.java)
}


inline fun <T> T.ifTrue(
    condition: () -> Boolean,
    process: (T) -> Unit,
): T {
    if (condition()) {
        process(this)
    }
    return this
}

inline fun <reified T> Array<T>.shuffled(): Array<T> {
    val list = toMutableList()
    list.shuffle()
    return list.toTypedArray()
}

inline fun <T, R> Lazy<T>.ifInitialized(block: (T) -> R) = if (isInitialized()) block(value) else null

val <T> Lazy<T>.valueIfInitialized get() = ifInitialized { value }

fun Lazy<AutoCloseable>.closeIfInitialized() = ifInitialized { value.close() }