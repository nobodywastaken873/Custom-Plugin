package me.newburyminer.customItems.items

import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.EquipmentSlot

interface ItemPredicate {
    fun slotMatches(e: EntityEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.entity is Player &&
                (e.entity as Player).inventory.getItem(slot).isItem(custom)
    }
    fun slotMatches(e: PlayerEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return e.player.inventory.getItem(slot).isItem(custom)
    }

    fun isOffCooldown(e: EntityEvent, custom: CustomItem, postfix: String = ""): Boolean {
        return e.entity is Player &&
                (e.entity as Player).offCooldown(custom, postfix)
    }
    fun isOffCooldown(e: PlayerEvent, custom: CustomItem, postfix: String = ""): Boolean {
        return e.player.offCooldown(custom, postfix)
    }
}