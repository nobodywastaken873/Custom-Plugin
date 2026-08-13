package me.newburyminer.customItems.items.customs.armor.sets.immunity

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.behaviors.ItemPredicate
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffectType

interface PotionEffectCancel: ItemPredicate {
    val potionEffects: List<PotionEffectType>
    fun potionEffectMatches(e: EntityPotionEffectEvent, slot: EquipmentSlot, custom: CustomItem): Boolean {
        return slotMatches(e, slot, custom) &&
                e.newEffect?.type in potionEffects &&
                (e.action == EntityPotionEffectEvent.Action.ADDED || e.action == EntityPotionEffectEvent.Action.CHANGED)
    }
    fun cancelPotionEffect(e: EntityPotionEffectEvent) {
        e.isCancelled = true
    }
}