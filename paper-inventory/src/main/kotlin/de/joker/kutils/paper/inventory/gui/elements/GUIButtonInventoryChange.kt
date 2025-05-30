package de.joker.kutils.paper.inventory.gui.elements

import de.joker.kutils.paper.inventory.gui.ForInventory
import de.joker.kutils.paper.inventory.gui.GUI
import de.joker.kutils.paper.inventory.gui.GUIClickEvent
import de.joker.kutils.paper.inventory.gui.InventoryChangeEffect
import de.joker.kutils.paper.inventory.gui.changeGUI
import de.joker.kutils.paper.inventory.gui.openGUIInstance
import org.bukkit.inventory.ItemStack

class GUIButtonInventoryChange<T : ForInventory>(
    icon: ItemStack,
    changeToGUICallback: () -> GUI<*>,
    changeToPageInt: Int?,
    onChange: ((GUIClickEvent<T>) -> Unit)?,
) : GUIButton<T>(icon, {
    val changeToGUI = changeToGUICallback.invoke().getInstance(it.player)
    val effect = (changeToGUI.gui.data.transitionTo ?: it.guiInstance.gui.data.transitionFrom)
        ?: InventoryChangeEffect.INSTANT
    val changeToPage = changeToGUI.getPage(changeToPageInt) ?: changeToGUI.currentPage

    changeToGUI.changeGUI(effect, it.guiInstance.currentPage, changeToPage)

    it.player.openGUIInstance(changeToGUI)

    onChange?.invoke(it)
})
