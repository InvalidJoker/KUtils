package de.joker.kutils.paper.coroutines


import dev.fruxz.ascend.extension.time.inWholeMinecraftTicks
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.isActive
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

@OptIn(InternalCoroutinesApi::class)
class PaperCoroutineDispatcher(
    private val bukkitPlugin: Plugin,
    private val isAsync: Boolean = true,
) : CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) return

        when {
            !isAsync && Bukkit.isPrimaryThread() -> block.run()
            !isAsync -> bukkitPlugin.server.scheduler.runTask(bukkitPlugin, block)
            else -> bukkitPlugin.server.scheduler.runTaskAsynchronously(bukkitPlugin, block)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        when {
            !isAsync -> bukkitPlugin.server.scheduler.runTaskLater(bukkitPlugin, Runnable { continuation.apply { resumeUndispatched(Unit) } }, timeMillis.milliseconds.inWholeMinecraftTicks)
            else -> bukkitPlugin.server.scheduler.runTaskLaterAsynchronously(bukkitPlugin, Runnable { continuation.apply { resumeUndispatched(Unit) } }, timeMillis.milliseconds.inWholeMinecraftTicks)
        }.let { task ->
            continuation.invokeOnCancellation { task.cancel() }
        }
    }

}

fun Plugin.pluginCoroutineDispatcher(isAsync: Boolean = true) = PaperCoroutineDispatcher(this, isAsync)