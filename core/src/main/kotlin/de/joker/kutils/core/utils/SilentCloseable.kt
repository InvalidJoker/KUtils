package de.joker.kutils.core.utils

import de.joker.kutils.core.extensions.println

/**
 * Extension of [AutoCloseable] which allows to close without handling Exceptions
 * @author Max Bossing
 * @since 0.0.1
 */
interface SilentCloseable : AutoCloseable {
    /**
     * Close silently (without throwing)
     * @return the thrown [Throwable] when applicable, or null
     * @since 0.0.1
     * @author Max Bossing
     */
    fun closeSilently(): Throwable? = runCatching { close() }.exceptionOrNull()

    /**
     * Close reporting, printing Exceptions to stdout
     * @since 0.0.1
     * @author Max Bossing
     */
    fun closeReporting() = runCatching { close() }.exceptionOrNull()?.println()
}