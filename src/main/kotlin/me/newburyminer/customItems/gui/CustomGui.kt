package me.newburyminer.customItems.gui

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class CustomGui: InventoryHolder {

    protected abstract val inv: Inventory
    override fun getInventory(): Inventory = inv

    abstract fun open(player: Player)

    open fun onClick(e: InventoryClickEvent) {}
    open fun onDrag(e: InventoryDragEvent) {}
    open fun onClose(e: InventoryCloseEvent) {}

}