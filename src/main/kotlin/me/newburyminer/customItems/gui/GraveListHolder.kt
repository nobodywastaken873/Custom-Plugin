package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.niceName
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class GraveListHolder(val player: Player, val page: Int): InventoryHolder {
    private val inventory = Bukkit.createInventory(this, 54, Utils.text("${player.name}'s graves"))
    private val graves = this.player.getListTag<Location>("gravelist") ?: mutableListOf()
    init {
        inventory.contents = GuiInventory.recipeInv.clone()
        if (page < graves.size / 35) inventory.setItem(53, GuiInventory.direction(true, page + 1))
        if (page > 0) inventory.setItem(45, GuiInventory.direction(false, page - 1))
        for (i in page*35..<(page+1)*35) {
            //add actual grave item here probably paper with coords, world, etc
            if (i >= graves.size) inventory.addItem(GuiInventory.gray(unique = true))
            else {
                val nextItem = ItemStack(Material.PAPER).lock().name(
                    Utils.text("Grave $i", arrayOf(247, 207, 2)),
                ).lore(
                    Utils.text("X: ${graves[i].x}, Y: ${graves[i].y}, Z: ${graves[i].z}", arrayOf(191, 191, 187)),
                    Utils.text("World: ${graves[i].world.niceName()}", arrayOf(191, 191, 187))
                ).setTag("grave", i)
                inventory.addItem(nextItem)
            }
        }
    }

    fun getLocation(item: ItemStack): Location {
        return graves[item.getTag<Int>("grave")!!]
    }

    override fun getInventory(): Inventory {
        return inventory
    }

}