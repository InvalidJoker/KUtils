package de.joker.kutils.paper.inventory.gui.elements

import de.joker.kutils.paper.inventory.gui.ForInventory
import de.joker.kutils.paper.inventory.gui.GUIClickEvent
import de.joker.kutils.paper.inventory.gui.GUISlot

class GUIFreeSlot<T : ForInventory> : GUISlot<T>() {
    override fun onClick(clickEvent: GUIClickEvent<T>) {
        /* do nothing */
    }
}
