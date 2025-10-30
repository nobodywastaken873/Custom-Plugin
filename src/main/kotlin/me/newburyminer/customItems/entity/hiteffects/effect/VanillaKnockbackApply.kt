package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class VanillaKnockbackApply(val strength: Double = 0.4): HitEffect {
    override val hitEffectType: HitEffectType = HitEffectType.VANILLA_KNOCKBACK

    override fun apply(victim: LivingEntity, damager: Entity) {
        val newStr = strength * (1.0 - (victim.getAttribute(Attribute.KNOCKBACK_RESISTANCE)?.value ?: 0.0))
        if (newStr <= 0) return
        val direction = victim.location.subtract(damager.location).toVector()
            .normalize()
            .multiply(newStr)
        val oldVel = victim.velocity

        val newVel = Vector(
            oldVel.x / 2 - direction.x,
            (oldVel.y / 2 + newStr).coerceAtLeast(0.4),
            oldVel.z / 2 - direction.z
        )

        victim.velocity = newVel
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "strength" to strength
        )
    }
    override fun deserialize(map: Map<String, Any>): HitEffect {
        return VanillaKnockbackApply(
            map["strength"] as Double
        )
    }
}