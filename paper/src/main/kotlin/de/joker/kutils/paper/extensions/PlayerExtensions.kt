package de.joker.kutils.paper.extensions

import dev.fruxz.stacked.extension.Times
import dev.fruxz.stacked.extension.Title
import dev.fruxz.stacked.text
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

fun Player.soundExecution() {
    playSound(location, Sound.ENTITY_ITEM_PICKUP, .75F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_LEATHER, .25F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_CHAIN, .1F, 2F)
}

fun UUID.toPlayer(): Player? = Bukkit.getPlayer(this)

fun String.toUUID(): UUID = UUID.fromString(this)

fun Player.toOfflinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(uniqueId)

fun Player.sendDeniedSound() = playSound(location, "minecraft:block.note_block.bass", 1f, 1f)

fun Player.sendSuccessSound() = playSound(location, "minecraft:block.note_block.pling", 1f, 1f)

fun Player.sendTeleportSound() = playSound(location, "minecraft:block.note_block.harp", 1f, 1f)

fun Player.sendOpenSound() = playSound(location, "minecraft:block.note_block.chime", 1f, 1f)

fun Player.maxOutHealth() {
    health = getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
}

val Player.standingOn: Block
    get() = world.getBlockAt(location.blockX, location.blockY - 1, location.blockZ)

val String.asPlayer: Player?
    get() = Bukkit.getPlayer(this)

fun UUID.toOfflinePlayer(): OfflinePlayer {
    return Bukkit.getOfflinePlayer(this)
}

fun String.toOfflinePlayer(): OfflinePlayer {
    return try {
        UUID.fromString(this).toOfflinePlayer()
    } catch (e: IllegalArgumentException) {
        Bukkit.getOfflinePlayer(this)
    }
}

fun String.toOfflinePlayerIfCached(): OfflinePlayer? {
    return Bukkit.getOfflinePlayerIfCached(this)
}

fun Player.title(
    title: String? = null,
    subtitle: String? = null,
    fadeIn: Int = 20,
    stay: Int = 60,
    fadeOut: Int = 20
) {
    showTitle(
        Title(
            title = text(title ?: " "),
            subtitle = text(subtitle ?: " "),
            times = Times(fadeIn.minecraftTicks, stay.minecraftTicks, fadeOut.minecraftTicks)
        )
    )
}
val LivingEntity.nms
    get() = (this as CraftLivingEntity).handle


val Player.packetConnection: ServerGamePacketListenerImpl
    get() {
        val serverPlayer = (player as CraftPlayer).handle as ServerPlayer
        return serverPlayer.connection
    }

val Player.nms: ServerPlayer
    get() = (this as CraftPlayer).handle

fun Player.actionBar(text: String) {
    sendActionBar(text(text))
}

val Player.locale: Locale
    get() = locale()

val Player.localeTag: String
    get() = locale().toLanguageTag()

fun broadcastActionbar(component: Component) {
    Bukkit.getOnlinePlayers().forEach { it.sendActionBar(component) }
}

var Player.scale: Double
    get() = getAttribute(Attribute.SCALE)?.baseValue ?: 1.0
    set(value) {
        getAttribute(Attribute.SCALE)?.baseValue = value
    }