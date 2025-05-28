package de.joker.kutils.paper.inventory.gui.elements

import de.joker.kutils.paper.inventory.gui.ForInventory
import de.joker.kutils.paper.inventory.gui.GUIClickEvent
import de.joker.kutils.paper.inventory.gui.GUIElement
import org.bukkit.inventory.ItemStack

class GUIPlaceholder<T : ForInventory>(
    private val icon: ItemStack,
) : GUIElement<T>() {
    override fun getItemStack(slot: Int) = icon
    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
    }
}
