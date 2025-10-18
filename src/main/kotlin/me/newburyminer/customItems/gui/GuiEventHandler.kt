package me.newburyminer.customItems.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class GuiEventHandler: Listener {

    @EventHandler fun onInventoryClick(e: InventoryClickEvent) {
        val holder = e.inventory.holder as? CustomGui ?: return
        holder.onClick(e)
    }

    @EventHandler fun onInventoryDrag(e: InventoryDragEvent) {
        val holder = e.inventory.holder as? CustomGui ?: return
        holder.onDrag(e)
    }

    @EventHandler fun onInventoryClose(e: InventoryCloseEvent) {
        val holder = e.inventory.holder as? CustomGui ?: return
        holder.onClose(e)
    }

}