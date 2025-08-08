package de.joker.kutils.velocity.extensions

import com.velocitypowered.api.proxy.Player
import de.joker.kutils.velocity.main.PluginInstance
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

fun UUID.toPlayer(): Player? {
    return PluginInstance.server.getPlayer(this).getOrNull()
}