package de.joker.kutils.core.notifications

import kotlin.reflect.KClass

interface NotificationHandler<T : Notification> {
    val handledType: KClass<T>
    suspend fun handleEvent(event: T)
}