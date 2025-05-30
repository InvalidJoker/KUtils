package de.joker.kutils.paper.inventory.gui.elements

import de.joker.kutils.paper.inventory.gui.ForInventory
import de.joker.kutils.paper.inventory.gui.GUIClickEvent
import de.joker.kutils.paper.inventory.gui.GUIElement
import org.bukkit.inventory.ItemStack

open class GUIButton<T : ForInventory>(
    private val icon: ItemStack,
    private val action: (GUIClickEvent<T>) -> Unit,
) : GUIElement<T>() {
    final override fun getItemStack(slot: Int) = icon
    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
        action(clickEvent)
    }
}
