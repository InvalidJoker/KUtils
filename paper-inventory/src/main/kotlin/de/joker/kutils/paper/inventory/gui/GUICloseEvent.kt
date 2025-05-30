package de.joker.kutils.paper.inventory.gui

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

class GUICloseEvent<T : ForInventory>(
    val bukkitEvent: InventoryCloseEvent,
    val guiInstance: GUIInstance<T>,
    val player: Player,
)
