package de.joker.kutils.core.tools

import java.security.SecureRandom
import java.util.Random
import kotlin.math.ceil

object NanoId {

    private const val DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val DEFAULT_SIZE = 21
    private val DEFAULT_RANDOM = SecureRandom()

    fun generate(
        size: Int = DEFAULT_SIZE,
        alphabet: String = DEFAULT_ALPHABET,
        additionalBytesFactor: Double = 1.6,
        random: Random = DEFAULT_RANDOM
    ): String {
        require(alphabet.isNotEmpty() && alphabet.length < 256)
        require(size > 0)
        require(additionalBytesFactor >= 1)

        val mask = calculateMask(alphabet)
        val step = calculateStep(size, alphabet, additionalBytesFactor)

        return generateOptimized(size, alphabet, mask, step, random)
    }

    fun generateOptimized(
        size: Int,
        alphabet: String,
        mask: Int,
        step: Int,
        random: Random = DEFAULT_RANDOM
    ): String {
        val id = StringBuilder(size)
        val bytes = ByteArray(step)

        while (true) {
            random.nextBytes(bytes)
            for (b in bytes) {
                val index = b.toInt() and mask
                if (index < alphabet.length) {
                    id.append(alphabet[index])
                    if (id.length == size) return id.toString()
                }
            }
        }
    }

    fun calculateMask(alphabet: String): Int {
        return (2 shl (Integer.SIZE - 1 - Integer.numberOfLeadingZeros(alphabet.length - 1))) - 1
    }

    fun calculateStep(size: Int, alphabet: String, additionalBytesFactor: Double): Int {
        val mask = calculateMask(alphabet)
        return ceil(additionalBytesFactor * mask * size / alphabet.length).toInt()
    }
}