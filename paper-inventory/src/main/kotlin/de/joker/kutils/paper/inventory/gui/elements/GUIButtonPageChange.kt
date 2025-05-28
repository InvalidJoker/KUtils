package de.joker.kutils.paper.inventory.gui.elements

import de.joker.kutils.paper.inventory.gui.ForInventory
import de.joker.kutils.paper.inventory.gui.GUIClickEvent
import de.joker.kutils.paper.inventory.gui.GUIPageChangeCalculator
import de.joker.kutils.paper.inventory.gui.PageChangeEffect
import de.joker.kutils.paper.inventory.gui.changePage
import org.bukkit.inventory.ItemStack

class GUIButtonPageChange<T : ForInventory>(
    icon: ItemStack,
    calculator: GUIPageChangeCalculator,
    shouldChange: ((GUIClickEvent<T>) -> Boolean)?,
    onChange: ((GUIClickEvent<T>) -> Unit)?,
) : GUIButton<T>(icon, {
    val currentPage = it.guiInstance.currentPage
    val newPage = it.guiInstance.getPage(
        calculator.calculateNewPage(
            it.guiInstance.currentPageInt,
            it.guiInstance.gui.data.pages.keys
        )
    )
    if (newPage != null) {
        val changePage = shouldChange?.invoke(it) ?: true

        if (changePage) {
            val effect = (newPage.transitionTo ?: currentPage.transitionFrom)
                ?: PageChangeEffect.INSTANT

            it.guiInstance.changePage(effect, currentPage, newPage)
            onChange?.invoke(it)
        }
    }
})
