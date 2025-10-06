package me.newburyminer.customItems.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable

class DurabilityListener: Listener {
    @EventHandler fun onItemBreak(e: PlayerItemBreakEvent) {
        if (e.brokenItem.itemMeta is ArmorMeta) {
            if (e.player.inventory.firstEmpty() == -1) {
                e.player.sendMessage(Utils.text("An item of yours has broken and dropped on the floor.", Utils.FAILED_COLOR))
            }
            e.player.addItemorDrop(e.brokenItem.clone())
        } else {
            e.brokenItem.amount += 1
        }
    }

    @EventHandler fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.inventory.itemInMainHand.type == Material.AIR) return
        if (!e.player.inventory.itemInMainHand.hasData(DataComponentTypes.MAX_DAMAGE)) return
        val newMeta = e.player.inventory.itemInMainHand.itemMeta as Damageable
        val maxDura = if (newMeta.hasMaxDamage()) newMeta.maxDamage else e.player.inventory.itemInMainHand.type.maxDurability.toInt()
        if (newMeta.damage == maxDura) e.isCancelled = true
    }
    @EventHandler fun onEntityDamageEntity(e: EntityDamageByEntityEvent) {
        playerDamageOther(e)
        playerDamagedByOther(e)
    }
    private fun playerDamageOther(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        val player = e.damager as Player
        if (player.inventory.itemInMainHand.type == Material.AIR) return
        if (!player.inventory.itemInMainHand.hasData(DataComponentTypes.MAX_DAMAGE)) return
        val newMeta = player.inventory.itemInMainHand.itemMeta as Damageable
        val maxDura = if (newMeta.hasMaxDamage()) newMeta.maxDamage else player.inventory.itemInMainHand.type.maxDurability.toInt()
        if (newMeta.damage == maxDura) e.isCancelled = true
    }
    private fun playerDamagedByOther(e: EntityDamageByEntityEvent) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        for (item in player.inventory.armorContents) {
            if (item == null) continue
            if (item.type == Material.AIR) continue
            val newMeta = item.itemMeta as Damageable
            val maxDura = if (newMeta.hasMaxDamage()) newMeta.maxDamage else item.type.maxDurability.toInt()
            if (newMeta.damage == maxDura) e.damage *= 1.5
        }
    }
    @EventHandler fun onInventoryChange(e: InventoryClickEvent) {
        //e.whoClicked.sendMessage(e.action.toString())
        //e.whoClicked.sendMessage(e.slot.toString())
        if (e.slotType != InventoryType.SlotType.ARMOR && e.action != InventoryAction.MOVE_TO_OTHER_INVENTORY) return
        if (e.action !in arrayOf(
                InventoryAction.HOTBAR_SWAP, InventoryAction.PLACE_ALL, InventoryAction.PLACE_FROM_BUNDLE, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME,
                InventoryAction.PLACE_SOME_INTO_BUNDLE, InventoryAction.SWAP_WITH_CURSOR, InventoryAction.MOVE_TO_OTHER_INVENTORY
        )) return
        val item = if (e.action == InventoryAction.HOTBAR_SWAP) {
            if (e.hotbarButton == -1) {
                e.whoClicked.inventory.itemInOffHand
            } else {
                e.whoClicked.inventory.getItem(e.hotbarButton) ?: return
            }
        } else if (e.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            e.whoClicked.inventory.getItem(e.slot) ?: return
        } else {
            e.cursor
        }

        if (!item.hasData(DataComponentTypes.MAX_DAMAGE)) return
        val newMeta = item.itemMeta as Damageable
        val maxDura = if (newMeta.hasMaxDamage()) newMeta.maxDamage else item.type.maxDurability.toInt()
        if (newMeta.damage == maxDura) e.isCancelled = true
    }
    @EventHandler fun onPlayerInteract(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.item!!.type == Material.AIR) return
        if (!e.item!!.hasData(DataComponentTypes.MAX_DAMAGE)) return
        if (e.item!!.hasData(DataComponentTypes.UNBREAKABLE)) return
        val newMeta = e.item!!.itemMeta as Damageable
        val maxDura = if (newMeta.hasMaxDamage()) newMeta.maxDamage else e.item!!.type.maxDurability.toInt()
        if (newMeta.damage == maxDura) e.isCancelled = true
        //e.player.sendMessage("cancelled")
    }
}