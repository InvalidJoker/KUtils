package de.joker.kutils.paper.event

import de.joker.kutils.core.annotation.KUtilsInternal
import de.joker.kutils.paper.PluginInstance
import de.joker.kutils.paper.coroutines.taskRunLater
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import kotlin.time.Duration

inline fun <reified T: Event> listen(autoRegistration: Boolean = true, crossinline block: (T) -> Unit) = listener { this.autoRegistration = autoRegistration; handler { event, _ -> block(event) } }

/**
 * Cancels a given event indefinitely until the listener is unregistered.
 * @param autoRegistration see [KListener.autoRegistration]
 * @param condition An optional [EventFilter] when to cancel (true = cancel; false = don't cancel)
 */
inline fun <reified T: Event> cancelEvent(autoRegistration: Boolean = true, noinline condition: EventFilter<T>? = null) = listener<T> { this.autoRegistration = autoRegistration; filter { if (condition != null) !condition(it) else false } }

/**
 * Creates a new [KListener]
 * @param T The [Event] to listen for
 * @param listener The [KListener] builder
 * @see [KListener]
 */
inline fun <reified T : Event> listener(listener: KListener<T>.() -> Unit) =
    KListener<T>()
        .apply(listener)
        .also { if (it.autoRegistration) it.register() }

typealias EventFilter<T> = (T) -> Boolean
typealias EventHandler<T> = (T, KListener<T>) -> Unit

/**
 * Represents a [Listener] that listens to only one [Event] and that can have multiple [EventFilter]s and [EventHandler]s
 * @since 0.0.1
 * @author Max Bossing
 * @see listener
 */
@OptIn(KUtilsInternal::class)
class KListener<T : Event> : Listener {
    private val filters = mutableListOf<EventFilter<T>>()
    private val handlers = mutableListOf<EventHandler<T>>()

    /**
     * How many times the listener has been executed
     *
     * do not modify this if you don't specifically want to extend the lifetime
     *
     * @see maxUses
     * @since 0.0.1
     */
    @KUtilsInternal
    var uses = 0

    /**
     * This Listeners [EventPriority]
     * @since 0.0.1
     */
    var priority: EventPriority = EventPriority.NORMAL

    /**
     * If this Listener should ignore previously cancelled events
     * @since 0.0.1
     */
    var ignoreCancelled: Boolean = false

    /**
     * If this Listener should be registered immediately
     * @since 0.0.1
     */
    var autoRegistration: Boolean = true

    /**
     * How many times this listener should be executed until it is unregistered automatically
     *
     * If null, infinite execution
     * @since 0.0.1
     */
    var maxUses: Int? = null

    /**
     * The Lifetime after which this Listener will be unregistered
     * @since 0.0.1
     */
    var lifeTime: Duration = Duration.INFINITE

    /**
     * The [BukkitTask] used to cancel the [KListener]
     *
     * Don't Modify!
     * @since 0.0.1
     */

    @KUtilsInternal
    var task: BukkitTask? = null

    /**
     * Add a new [EventFilter] to this Listener
     *
     * [EventFilter]s are executed before handlers, and if any filter returns false, the execution is cancelled
     *
     * [uses] will only increment if the filters return successfully
     * @since 0.0.1
     */
    fun filter(filter: EventFilter<T>) = filters.add(filter)

    /**
     * Add a new [EventHandler] to this Listener
     *
     * [EventHandler]s are executed in the order in which they have been added, only if the [EventFilter]s all return successfully
     * @since 0.0.1
     *
     */
    fun handler(handler: EventHandler<T>) = handlers.add(handler)

    @PublishedApi
    internal fun onEvent(event: T) {
        maxUses?.let { if (uses >= it) this.unregister() }
        if (filters.any { !it(event) }) return
        handlers.forEach { it(event, this) }
        uses++
    }
}

/**
 * Unregister a [KListener]
 *
 * If [KListener.task] is set, it will be cancelled
 * @see KListener
 * @author Max Bossing
 * @since 0.0.1
 */
@OptIn(KUtilsInternal::class)
fun KListener<*>.unregister() {
    HandlerList.unregisterAll(this)
    this.task?.cancel()
    this.task = null
}

/**
 * Register a [KListener]
 *
 * Its uses and lifetime will be reset
 * @author Max Bossing
 * @since 0.0.1
 */
@OptIn(KUtilsInternal::class)
inline fun <reified T : Event> KListener<T>.register() {
    Bukkit.getPluginManager().registerEvent(
        T::class.java,
        this,
        priority,
        { _, event -> (event as? T)?.let { this.onEvent(it) } },
        PluginInstance,
        ignoreCancelled
    )
    this.uses = 0
    task?.cancel()
    task = null
    lifeTime
        .takeIf { it != Duration.INFINITE }
        ?.let {
            this.task = taskRunLater(it.inWholeMilliseconds.coerceAtLeast(50) / 50) {
                this.unregister()
                this.task = null
            }
        }
}

fun Listener.unregister() = HandlerList.unregisterAll(this)