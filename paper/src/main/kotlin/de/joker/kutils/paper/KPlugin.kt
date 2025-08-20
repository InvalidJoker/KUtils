package de.joker.kutils.paper

import de.joker.kutils.core.annotation.KUtilsInternal
import de.joker.kutils.paper.coroutines.pluginCoroutineDispatcher
import de.joker.kutils.paper.event.custom.CustomEventListener
import de.joker.kutils.paper.modules.PaperModule
import de.joker.kutils.paper.tasks.KRunnableHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.plugin.java.JavaPlugin

/**
 * The main plugin instance. Less complicated name for internal usage.
 */
lateinit var PluginInstance: KPlugin

@OptIn(KUtilsInternal::class)
abstract class KPlugin : JavaPlugin() {
    private val kRunnableHolderProperty = lazy { KRunnableHolder }
    internal val kRunnableHolder by kRunnableHolderProperty

    val syncDispatcher by lazy { pluginCoroutineDispatcher(false) }
    val asyncDispatcher by lazy { pluginCoroutineDispatcher(true) }

    val coroutineScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + coroutineScope.coroutineContext) }

    /**
     * [PaperModule]s that will be integrated into the Plugin lifecycle
     */
    open val modules: List<PaperModule>? = null

    /**
     * Called when the plugin was loaded
     */
    open fun load() {}

    /**
     * Called when the plugin was enabled
     */
    open fun startup() {}

    /**
     * Called when the plugin gets disabled
     */
    open fun shutdown() {}

    final override fun onLoad() {
        if (::PluginInstance.isInitialized) {
            logger.warning("The main instance has been modified, even though it has already been set by another plugin!")
        }
        PluginInstance = this
        modules?.forEach { it.load(this) }
        load()
    }

    final override fun onEnable() {
        CustomEventListener.load()

        modules?.forEach { it.start(this) }
        startup()
    }

    final override fun onDisable() {
        CustomEventListener.unload()
        shutdown()
        modules?.forEach { it.stop(this) }
        if (kRunnableHolderProperty.isInitialized()) kRunnableHolderProperty.value.close()
    }
}
