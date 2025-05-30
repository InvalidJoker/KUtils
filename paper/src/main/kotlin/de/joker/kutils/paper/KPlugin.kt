package de.joker.kutils.paper

import de.joker.kutils.paper.coroutines.pluginCoroutineDispatcher
import de.joker.kutils.paper.event.custom.CustomEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.plugin.java.JavaPlugin

/**
 * The main plugin instance. Less complicated name for internal usage.
 */
lateinit var PluginInstance: KPlugin


abstract class KPlugin : JavaPlugin() {
    val syncDispatcher by lazy { pluginCoroutineDispatcher(false) }
    val asyncDispatcher by lazy { pluginCoroutineDispatcher(true) }

    val coroutineScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + coroutineScope.coroutineContext) }

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
        load()
    }

    final override fun onEnable() {
        CustomEventListener.load()
        startup()
    }

    final override fun onDisable() {
        CustomEventListener.unload()
        shutdown()
    }
}
