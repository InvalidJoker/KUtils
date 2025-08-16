package de.joker.kutils.paper.tasks

import de.joker.kutils.core.annotation.KUtilsInternal
import de.joker.kutils.paper.PluginInstance
import org.bukkit.scheduler.BukkitRunnable

/**
 * Represents a [BukkitRunnable] with additional properties
 * @property counterUp Counts up the cycles if the task is infinite
 * @property counterDown Counts down the cycles until the task ends if `uses` is set in [task] (counts to zero)
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @author Max Bossing
 * @since 0.0.1
 * @see task
 */

abstract class KTask(
    var counterUp: Long? = null,
    var counterDown: Long? = null
) : BukkitRunnable()

/**
 * Creates a new [KTask]
 *
 * @param sync if it should run synchronous (true) or asynchronous (false)
 * @param delay Delay (in ticks) until the first execution
 * @param period Interval (in ticks) between executions
 * @param uses How many times the task should be executed - null for infinite
 * @param safeCallback If the endCallback should always be executed, even on shutdown or premature end
 * @param endCallback Callback that is executed when the task ends
 * @param task The [KTask] that will be executed every repetition
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 * @see [KTask]
 */
@OptIn(KUtilsInternal::class)
fun task(
    sync: Boolean = true,
    delay: Long = 0,
    period: Long? = 20,
    uses: Long? = null,
    safeCallback: Boolean = false,
    endCallback: (() -> Unit)? = null,
    task: ((KTask) -> Unit)
): KTask? {
    uses?.let { if (it == 0L) return null }

    object : KTask() {
        private var counter = 0L
        override fun run() {
            var shouldCancel = false
            uses?.let {
                counterDown = uses - counter - 1
                counter++

                if (counter >= uses)
                    shouldCancel = true

                counterUp = counter
            }
            task(this)

            if (shouldCancel) cancel()

            if (isCancelled) {
                if (shouldCancel || safeCallback)
                    KRunnableHolder.activate(this)
                else
                    KRunnableHolder.remove(this)
            }
        }
    }.let { kTask ->
        endCallback?.let { KRunnableHolder.add(kTask, it, safeCallback) }
        period?.let {
            if (sync) kTask.runTaskTimer(PluginInstance, delay, period)
            else kTask.runTaskTimerAsynchronously(
                PluginInstance,
                delay,
                period
            )
        }

        return kTask
    }
}
