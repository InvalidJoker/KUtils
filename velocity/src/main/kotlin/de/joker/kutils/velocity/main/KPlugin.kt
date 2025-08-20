package de.joker.kutils.velocity.main

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.fruxz.ascend.extension.logging.getThisFactoryLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * The main plugin instance. Less complicated name for internal usage.
 */
lateinit var PluginInstance: KPlugin

/**
 * The main plugin class for Velocity plugins.
 * This class provides a structure for plugins to follow,
 *
 * including methods for startup and shutdown,
 * needs to be annotated with @Plugin to be recognized by Velocity.
 */
abstract class KPlugin(
    val server: ProxyServer,
) {
    val coroutineScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + coroutineScope.coroutineContext) }

    /**
     * Called when the plugin was enabled
     */
    open fun startup() {}

    /**
     * Called when the plugin gets disabled
     */
    open fun shutdown() {}

    @Subscribe
    fun onInit(ignored: ProxyInitializeEvent) {
        if (::PluginInstance.isInitialized) {
            getThisFactoryLogger().warn("The main instance has been modified, even though it has already been set by another plugin!")
        }
        PluginInstance = this
        startup()
    }

    @Subscribe
    fun onShutdown(ignored: ProxyShutdownEvent) {
        shutdown()
    }

}