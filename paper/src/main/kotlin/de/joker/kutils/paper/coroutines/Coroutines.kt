package de.joker.kutils.paper.coroutines

import de.joker.kutils.paper.PluginInstance
import de.joker.kutils.paper.extensions.timeLimit
import org.bukkit.Bukkit
import kotlinx.coroutines.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

fun sync(block: () -> Unit) {
    Bukkit.getScheduler().runTask(PluginInstance, block)
}

fun async(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(PluginInstance, block)
}

/**
 * mcroutine guarantees execution on the server thread.
 * True blocking is illegal, see [mcasync] for options.
 */
fun <T> mcroutine(coroutine: suspend () -> T) {
    CoroutineScope(Dispatchers.mc).launch {
        coroutine()
    }
}

/**
 * mcasync guarantees execution away from the server thread.
 * True blocking is OK, mcasync uses an Unbound thread-pool, so it's
 * not an option for CPU bound tasks.
 */
suspend fun <T> mcasync(coroutine: suspend () -> T): T {
    return withContext(Dispatchers.async) {
        try {
            coroutine()
        } catch (e: Exception) {
            throw e
        }
    }
}

fun <T> mcasyncBlocking(coroutine: suspend () -> T) {
    CoroutineScope(Dispatchers.async).launch {
        coroutine()
    }
}


val globalPool: ExecutorService = Executors.newCachedThreadPool()

object MinecraftCoroutineDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getServer().scheduler.runTask(PluginInstance, block)
            return
        }
        block.run()
    }
}

object AsyncCoroutineDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.isPrimaryThread()) {
            globalPool.execute(block)
            return
        }
        block.run()
    }
}

val Dispatchers.async: CoroutineContext
    get() = AsyncCoroutineDispatcher

val Dispatchers.mc: CoroutineContext
    get() = MinecraftCoroutineDispatcher

/**
 * Executes the given [runnable] with the given [delay].
 * Either sync or async (specified by the [sync] parameter).
 */
fun taskRunLater(delay: Long, sync: Boolean = true, runnable: () -> Unit) {
    if (sync)
        Bukkit.getScheduler().runTaskLater(PluginInstance, runnable, delay)
    else
        Bukkit.getScheduler().runTaskLaterAsynchronously(PluginInstance, runnable, delay)
}

/**
 * Executes the given [runnable] either
 * sync or async (specified by the [sync] parameter).
 */
fun taskRun(sync: Boolean = true, runnable: () -> Unit) {
    if (sync) {
        sync(runnable)
    } else {
        async(runnable)
    }
}

fun taskRunTimer(period: Long = 1, name: String = "", block: () -> Unit): BukkitTask {
    return object : BukkitRunnable() {
        override fun run() = timeLimit(period, name, block)
    }.runTaskTimer(PluginInstance, 0, period)
}
fun task(
    period: Long = 1,
    howOften: Long = 1,
    name: String = "",
    sync: Boolean = true,
    block: (BukkitTask) -> Unit
): BukkitTask {
    var count = 0L

    lateinit var task: BukkitTask

    val runnable = object : BukkitRunnable() {
        override fun run() {
            timeLimit(period, name) {
                block(task)

                count++
                if (count >= howOften) {
                    cancel()
                }
            }
        }
    }

    task = if (sync) {
        runnable.runTaskTimer(PluginInstance, 0, period)
    } else {
        runnable.runTaskTimerAsynchronously(PluginInstance, 0, period)
    }

    return task
}