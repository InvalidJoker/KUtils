package de.joker.kutils.velocity.extensions

import com.velocitypowered.api.proxy.server.RegisteredServer
import de.joker.kutils.velocity.main.PluginInstance

fun getServerOrNull(
    serverName: String? = null
): RegisteredServer? {
    return PluginInstance.server.getServer(serverName)
        .orElse(null)
}

fun getServer(
    serverName: String? = null
): RegisteredServer {
    return getServerOrNull(serverName)
        ?: throw IllegalArgumentException("Server with name '$serverName' not found")
}

val allServers: Collection<RegisteredServer>
    get() = PluginInstance.server.allServers