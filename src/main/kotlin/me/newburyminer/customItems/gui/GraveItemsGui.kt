package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class GraveItemsGui(private val armorStand: Interaction): CustomGui() {

    override val inv: Inventory
    init {
        val items = armorStand.getListTag<ItemStack>("graveitems")!!
        val owner = Bukkit.getPlayer(armorStand.getTag<UUID>("owner")!!) ?: Bukkit.getOfflinePlayer(armorStand.getTag<UUID>("owner")!!)
        val text = text("${owner.name}'s grave", arrayOf(199, 4, 30))
        inv = Bukkit.createInventory(this, 54, text)
        inv.contents = items.toTypedArray()
    }

    override fun open(player: Player) {
        player.openInventory(inv)
        armorStand.setTag("currentlyopen", true)
        CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_OPEN, 1.0F, 1.2F)
    }

    override fun onClick(e: InventoryClickEvent) {
        updateGrave()
        if (this.inv.isEmpty) Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            inv.close()
        })
    }

    override fun onDrag(e: InventoryDragEvent) {
        updateGrave()
        if (this.inv.isEmpty) Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            inv.close()
        })
    }

    override fun onClose(e: InventoryCloseEvent) {
        if (this.inv.isEmpty) deleteGrave()
        armorStand.setTag("currentlyopen", false)
    }

    private fun updateGrave() {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            this.armorStand.removeTag("graveitems")
            this.armorStand.setListTag("graveitems", this.inv.contents.toMutableList().filterNotNull().toMutableList())
        })
    }

    private fun deleteGrave() {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {

            // remove from player's grave list
            val uuid = this.armorStand.getTag<UUID>("owner")!!
            val player: Player? = if (Bukkit.getServer().getPlayer(uuid) == null) Bukkit.getServer().getOfflinePlayer(uuid).player else Bukkit.getServer().getPlayer(uuid)
            if (player != null) {
                val graves = player.getListTag<Location>("gravelist")!!
                for (i in graves.indices.reversed()) {
                    if (armorStand.location.clone().world != graves[i].world) continue
                    if (armorStand.location.clone().subtract(Vector(0.5, 0.0, 0.5)).subtract(graves[i]).length() < 0.5) {
                        graves.removeAt(i)
                        break
                    }
                }
                player.removeTag("gravelist")
                player.setListTag("gravelist", graves)
            }

            // remove grave title, grave block display, and interation entity
            for (entity in this.armorStand.location.subtract(0.5, 0.0, 0.5).getNearbyEntities(0.1, 0.1, 0.1))  {
                if (entity.type == EntityType.BLOCK_DISPLAY) {entity.remove(); break}
            }
            for (entity in this.armorStand.location.add(0.0,1.2, 0.0).getNearbyEntities(0.1, 0.1, 0.1)) {
                if (entity.type == EntityType.TEXT_DISPLAY) {entity.remove(); break}
            }
            this.armorStand.remove()
        })
    }



}