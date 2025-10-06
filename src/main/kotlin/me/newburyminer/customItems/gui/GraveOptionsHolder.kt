package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class GraveOptionsHolder(val player: Player, val location: Location, val page: Int): InventoryHolder {
    private val inventory = Bukkit.createInventory(this, 27)
    init {
        for (i in 0..26) {
            inventory.setItem(i, GuiInventory.gray())
        }
        inventory.setItem(12, ItemStack(Material.RED_CONCRETE).lock().name(
            Utils.text("Delete grave", arrayOf(247, 207, 2)),
        ).loreBlock(
            Utils.text("This will remove the grave from this list, the grave will not be removed from the world.", arrayOf(191, 191, 187))
        ))

        val cost = (player.getTag<Int>("totalgravetps") ?: 0) + 1

        inventory.setItem(14, ItemStack(Material.ENDER_PEARL).lock().name(
            Utils.text("Teleport to grave", arrayOf(247, 207, 2)),
        ).loreBlock(
            Utils.text("This will teleport you to the exact location of the grave, you will recieve 5 seconds of stability and invulnerability.", arrayOf(191, 191, 187)),
            Utils.text(""),
            Utils.text("Cost: ", arrayOf(191, 191, 187)),
            Utils.text("$cost Diamonds", arrayOf(92, 237, 225))
        ))
        inventory.setItem(22, GuiInventory.back(page))
    }

    //can add cost later here
    fun teleport() {
        player.getAttribute(Attribute.GRAVITY)?.removeModifier(NamespacedKey(CustomItems.plugin, "gravity"))
        player.getAttribute(Attribute.GRAVITY)?.addModifier(AttributeModifier(
            NamespacedKey(CustomItems.plugin, "gravity"),
            -1.0,
            AttributeModifier.Operation.MULTIPLY_SCALAR_1
        ))
        player.isInvulnerable = true
        player.setTag("graveinvulnerability", 25)
        player.teleport(location.clone().add(Vector(0.5, 0.0, 0.5)))
    }

    fun delete() {
        val graveLocs = player.getListTag<Location>("gravelist") ?: mutableListOf()
        for (i in graveLocs.indices.reversed()) {
            if (graveLocs[i] == location) {
                graveLocs.removeAt(i)
                break
            }
        }
        player.setListTag("gravelist", graveLocs)
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}