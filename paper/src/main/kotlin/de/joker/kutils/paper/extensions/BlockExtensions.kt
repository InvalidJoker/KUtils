package de.joker.kutils.paper.extensions

import org.bukkit.Material
import org.bukkit.block.Barrel
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.Container
import org.bukkit.inventory.ItemStack

fun Block.getConnectedStorageContainers() = sequenceOf(
    getRelative(1, 0, 0),
    getRelative(-1, 0, 0),
    getRelative(0, 1, 0),
    getRelative(0, -1, 0),
    getRelative(0, 0, 1),
    getRelative(0, 0, -1)
)
    .filter { it.state is Container }
    .map { it.state as Container }
    .filter { it is Chest || it is Barrel }

fun Material.asQuantity(amount: Int): ItemStack {
    return ItemStack(this, amount)
}