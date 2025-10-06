package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Bukkit
import org.bukkit.block.ShulkerBox
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
//fix player hotkey event, drop item event for shulker, maybe implement anti dupe within the update shulker
//check if shulker is null, then prevent any actions inside of it until it is not null
class ShulkerHolder(val shulker: ItemStack): InventoryHolder {
    private var inventory: Inventory
    init {
        //for (player in Bukkit.getServer().onlinePlayers) player.sendMessage(shulker.type.name)
        val itemMeta = shulker.itemMeta as BlockStateMeta
        val blockState = itemMeta.blockState as ShulkerBox
        inventory = Bukkit.createInventory(this, InventoryType.SHULKER_BOX, blockState.customName() ?: Utils.text("Shulker Box"))
        inventory.contents = blockState.inventory.contents
    }

    fun updateShulker() {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            val newMeta = shulker.itemMeta as BlockStateMeta
            val newBlockState = newMeta.blockState as ShulkerBox
            val itemInventory = newBlockState.inventory
            itemInventory.contents = inventory.contents
            newMeta.blockState = newBlockState
            shulker.itemMeta = newMeta
            newBlockState.update()
        })
    }

    fun closeShulker() {
        this.shulker.setTag("shulkeropen", false)
    }

    override fun getInventory(): Inventory {
        return this.inventory
    }
}