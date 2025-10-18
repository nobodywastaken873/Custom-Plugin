package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils.Companion.name
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object GuiLayout {

    private val MAX_SIZE_BORDER = arrayOf(
         0,                              8,
         9,                             17,
        18,                             26,
        27,                             35,
        36,                             44,
        45, 46, 47, 48, 49, 50, 51, 52, 53
    )

    private val CRAFTING_BORDER = arrayOf(
         0,                      6,  7,  8,
         9,                     15, 16 ,17,
        18,                     24,     26,
        27,                     33, 34 ,35,
        36,                     42, 43, 44,
        45, 46, 47, 48, 49, 50, 51, 52, 53
    )

    private val CIRCLE_BORDER = arrayOf(
         0,  1,  2,  3,  4,  5,  6,  7,  8,
         9,                             17,
        18,                             26,
        27,                             35,
        36,                             44,
        45, 46, 47, 48, 49, 50, 51, 52, 53
    )

    fun setCraftingBorder(item: Material, inventory: Inventory) {
        CRAFTING_BORDER.forEach {
            inventory.setItem(it, GuiItems.getFiller(item))
        }
    }

    fun setMaxBorder(item: Material, inventory: Inventory) {
        MAX_SIZE_BORDER.forEach {
            inventory.setItem(it, GuiItems.getFiller(item))
        }
    }

    fun setCircleBorder(item: Material, inventory: Inventory) {
        CIRCLE_BORDER.forEach {
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