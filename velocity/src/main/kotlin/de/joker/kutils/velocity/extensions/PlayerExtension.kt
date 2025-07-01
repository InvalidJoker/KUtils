package de.joker.kutils.velocity.extensions

import com.velocitypowered.api.proxy.Player
import de.joker.kutils.velocity.main.PluginInstance
import dev.fruxz.stacked.text
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

fun Player.send(message: String) {
    this.sendMessage(text(message))
}

fun UUID.toPlayer(): Player? {
    return PluginInstance.server.getPlayer(this).getOrNull()
}