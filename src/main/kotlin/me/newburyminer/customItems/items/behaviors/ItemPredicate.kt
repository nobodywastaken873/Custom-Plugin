package me.newburyminer.customItems.items.behaviors

import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

interface ItemPredicate {
    fun slotMatches(e: EntityDamageByEntityEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.damager is Player &&
                (e.damager as Player).inventory.getItem(slot).isItem(custom)
    }
    fun slotMatches(e: EntityPushedByEntityAttackEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.pushedBy is Player &&
                (e.pushedBy as Player).inventory.getItem(slot).isItem(custom)
    }
    fun slotMatches(e: EntityEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.entity is Player &&
                (e.entity as Player).inventory.getItem(slot).isItem(custom)
    }
    fun slotMatches(e: PlayerEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.player.inventory.getItem(slot).isItem(custom)
    }
    fun inHand(e: PlayerEvent, custom: CustomItem): Boolean {
        return e.player.inventory.itemInMainHand.isItem(custom) || e.player.inventory.itemInOffHand.isItem(custom)
    }

    fun isOffCooldown(e: EntityEvent, custom: CustomItem, postfix: String = ""): Boolean {
        return e.entity is Player &&
                (e.entity as Player).offCooldown(custom, postfix)
    }
    fun isOffCooldown(e: PlayerEvent, custom: CustomItem, postfix: String = ""): Boolean {
        return e.player.offCooldown(custom, postfix)
    }

    fun isRightClick(e: PlayerInteractEvent): Boolean {
        return e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK
    }
    fun isLeftClick(e: PlayerInteractEvent): Boolean {
        return e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK
    }

    fun activeRangedMatches(e: ProjectileLaunchEvent, customItem: CustomItem): Boolean {
        val item = getActiveRanged(e) ?: return false
        return item.isItem(customItem)
    }
    fun getActiveRanged(e: ProjectileLaunchEvent): ItemStack? {
        val player = e.entity.shooter as? Player ?: return null
        val mainhand = player.inventory.itemInMainHand
        val offhand = player.inventory.itemInOffHand
        return if (mainhand.type in arrayOf(Material.BOW, Material.CROSSBOW) && mainhand.offCooldown(player))
            mainhand
        else
            offhand
    }
}