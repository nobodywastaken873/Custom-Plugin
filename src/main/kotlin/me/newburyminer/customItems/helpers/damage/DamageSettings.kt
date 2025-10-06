package me.newburyminer.customItems.helpers.damage

import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity

data class DamageSettings(val damage: Double, val damageType: DamageType? = null, val damager: Entity? = null, val knockback: KnockbackSettings? = null, val iframes: Int = 10) {
}