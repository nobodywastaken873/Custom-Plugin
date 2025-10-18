package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils.Companion.name
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object GuiLayout {

    private val MAX_SIZE_BORDER = arrayOf(
        0, 8, 9, 17, 18, 26, 27, 35, 36, 44,
        45, 46, 47, 48, 49, 50, 51, 52, 53
    )

    fun setMaxBorder(item: Material, inventory: Inventory) {
        MAX_SIZE_BORDER.forEach {
            inventory.setItem(it, GuiItems.getFiller(item))
        }
    }

    fun fillEmpty(item: Material, inventory: Inventory) {
        val emptySlots = inventory.contents.count { it == null }
        for (i in 0..<emptySlots)
            inventory.addItem(GuiItems.getFiller(item))
    }

    fun addArrows(currentPage: Int, totalPages: Int, inventory: Inventory) {
        val hasBackArrow = currentPage != 0
        val hasForwardArrow = currentPage < totalPages - 1

        if (hasBackArrow)
            inventory.setItem(45, GuiItems.PREVIOUS_PAGE)
        if (hasForwardArrow)
            inventory.setItem(53, GuiItems.NEXT_PAGE)
    }

    fun clearInventory(inventory: Inventory) {
        inventory.contents = arrayOf()
    }

}