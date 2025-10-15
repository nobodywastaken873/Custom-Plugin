package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class GraveHolder(private val armorStand: Interaction): InventoryHolder {
    private var inventory: Inventory
    init {
        val items = armorStand.getListTag<ItemStack>("graveitems")!!
        val owner = Bukkit.getPlayer(armorStand.getTag<UUID>("owner")!!) ?: Bukkit.getOfflinePlayer(armorStand.getTag<UUID>("owner")!!)
        val text = text("${owner.name}'s grave", arrayOf(199, 4, 30))
        inventory = Bukkit.createInventory(this, 54, text)
        inventory.contents = items.toTypedArray()
    }

    fun updateGrave() {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            this.armorStand.removeTag("graveitems")
            this.armorStand.setListTag("graveitems", this.inventory.contents.toMutableList().filterNotNull().toMutableList())
        })
    }

    fun closeGrave() {
        this.armorStand.setTag("currentlyopen", false)
    }

    fun deleteGrave() {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            for (entity in this.armorStand.location.subtract(0.5, 0.0, 0.5).getNearbyEntities(0.1, 0.1, 0.1))  {
                if (entity.type == EntityType.BLOCK_DISPLAY) {entity.remove(); break}
            }
            for (entity in this.armorStand.location.add(0.0,1.2, 0.0).getNearbyEntities(0.1, 0.1, 0.1)) {
                if (entity.type == EntityType.TEXT_DISPLAY) {entity.remove(); break}
            }
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

            this.armorStand.remove()
        })
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}