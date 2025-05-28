package de.joker.kutils.paper.inventory.gui

class GUIPage<T : ForInventory>(
    val number: Int,
    internal val slots: Map<Int, GUISlot<T>>,
    val transitionTo: PageChangeEffect?,
    val transitionFrom: PageChangeEffect?,
)
