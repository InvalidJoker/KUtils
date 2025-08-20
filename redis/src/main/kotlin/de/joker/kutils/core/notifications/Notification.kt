package de.joker.kutils.core.notifications

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
sealed interface Notification {
    val channel: String
    val eventName: String
    @OptIn(ExperimentalTime::class)
    val timestamp: Instant
}