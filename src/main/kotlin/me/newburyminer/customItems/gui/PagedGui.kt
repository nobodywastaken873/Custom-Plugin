package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils.Companion.getItemAction
import org.bukkit.event.inventory.InventoryClickEvent

abstract class PagedGui(protected var currentPage: Int): CustomGui() {

    fun checkForPageChange(e: InventoryClickEvent): Boolean {
        val clickedItem = e.clickedInventory?.getItem(e.slot) ?: return false
        val action = clickedItem.getItemAction() ?: return false
        e.isCancelled = true
        when (action) {

            ItemAction.NEXT_PAGE -> {
                openPage(currentPage + 1)
                ++currentPage
            }

            ItemAction.PREVIOUS_PAGE -> {
                openPage(currentPage - 1)
                --currentPage
            }

            else -> {
                return false
            }

        }
        return true
    }

    abstract fun openPage(newPage: Int)
}