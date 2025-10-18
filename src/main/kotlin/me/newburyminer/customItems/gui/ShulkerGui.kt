package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta

class ShulkerGui(private val shulker: ItemStack): CustomGui() {

    override val inv: Inventory
    init {
        //for (player in Bukkit.getServer().onlinePlayers) player.sendMessage(shulker.type.name)
        val itemMeta = shulker.itemMeta as BlockStateMeta
        val blockState = itemMeta.blockState as ShulkerBox
        inv = Bukkit.createInventory(this, InventoryType.SHULKER_BOX, blockState.customName() ?: Utils.text("Shulker Box"))
        inv.contents = blockState.inventory.contents
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    private fun updateShulker() {
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

    private fun closeShulker() {
        this.shulker.setTag("shulkeropen", false)
    }

    override fun onClick(e: InventoryClickEvent) {
        val clickedInventory = e.clickedInventory ?: return
        val clickedItem = clickedInventory.getItem(e.slot) ?: return

        if (clickedItem.getTag<Boolean>("shulkeropen") == true) {
            e.isCancelled = true
            CustomEffects.playSound(e.whoClicked.location, Sound.ENTITY_SHULKER_HURT, 1.0F, 1.2F)
            return
        }

        if (!Tag.SHULKER_BOXES.isTagged(shulker.type)) {
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.whoClicked.closeInventory()
                return@Runnable
            })
        }
        updateShulker()
    }

    override fun onDrag(e: InventoryDragEvent) {
        if (!Tag.SHULKER_BOXES.isTagged(shulker.type)) {
            e.isCancelled = true
        }
        updateShulker()
    }

    override fun onClose(e: InventoryCloseEvent) {
        updateShulker()
        closeShulker()
    }


}