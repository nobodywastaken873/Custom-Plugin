package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.DoubleRange
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class DisableShieldApply(private val ignoreDirection: Boolean = false): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity) {
        if (victim !is Player) return
        if (!victim.isBlocking) return

        val damagerLocation = damager.location
        val victimLocation = victim.location

        val damagerFacing = victimLocation.subtract(damagerLocation).toVector()
        val victimFacing = victimLocation.direction

        val isBlocked = isBlocking(damagerFacing, victimFacing)
        if (!isBlocked) return

        CustomEffects.playSound(victim.location, Sound.ITEM_SHIELD_BREAK, 1.0F, DoubleRange(0.8, 1.2))
        victim.setCooldown(Material.SHIELD, 100)
    }

    private fun isBlocking(hitterFacing: Vector, takerFacing: Vector): Boolean {
        return hitterFacing.normalize().dot(takerFacing.normalize()) < 0
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "ignoreDirection" to ignoreDirection,
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.DISABLE_SHIELD
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return DisableShieldApply(map["ignoreDirection"].toBoolean())
        }
    }
}