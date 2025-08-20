package de.joker.kutils.core.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.cast

class EventBus {
    val handlers = mutableMapOf<Class<out Notification>, suspend (Notification) -> Unit>()
    private val classHandlers = mutableMapOf<KClass<out Notification>, NotificationHandler<out Notification>>()
    private val scope = CoroutineScope(Dispatchers.IO)

    inline fun <reified T : Notification> onEvent(noinline handler: suspend (T) -> Unit) {
        handlers[T::class.java] = { event -> handler(event as T) }
    }

    fun <T : Notification> registerHandler(handler: NotificationHandler<T>) {
        classHandlers[handler.handledType] = handler
    }

    fun dispatch(event: Notification) {
        handlers[event::class.java]?.let { handler ->
            scope.launch { handler(event) }
        }
        classHandlers[event::class]?.let { handler ->
            scope.launch {
                handleTyped(handler, event)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : Notification> handleTyped(handler: NotificationHandler<T>, event: Notification) {
        val casted = handler.handledType.cast(event)
        handler.handleEvent(casted)
    }
}