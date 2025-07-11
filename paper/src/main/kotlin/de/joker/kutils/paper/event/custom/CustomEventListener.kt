package de.joker.kutils.paper.event.custom

import de.joker.kutils.paper.event.EventHandler
import de.joker.kutils.paper.event.listen
import de.joker.kutils.paper.event.register
import de.joker.kutils.paper.event.unregister
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object CustomEventListener: EventHandler {
    override fun load() {
        onPlayerInteract.register()
        onEntityDamageByEntity.register()
    }

    override fun unload() {
        onPlayerInteract.unregister()
        onEntityDamageByEntity.unregister()
    }

    private val onPlayerInteract = listen<PlayerInteractEvent> {
        val player = it.player

        val block = it.clickedBlock ?: return@listen

        Bukkit.getPluginManager().callEvent(PlayerInteractAtBlockEvent(player, block))
    }


    private val onEntityDamageByEntity = listen<EntityDamageByEntityEvent> {
        val damager = it.damager as? Player ?: return@listen
        val victim = it.entity as? Player ?: return@listen

        Bukkit.getPluginManager().callEvent(PlayerDamageByPlayerEvent(damager, victim, it.damage))

    }

}
