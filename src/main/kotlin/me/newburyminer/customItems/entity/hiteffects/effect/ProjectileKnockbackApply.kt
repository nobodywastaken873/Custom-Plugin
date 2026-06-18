package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class ProjectileKnockbackApply(val strength: Double = 0.4): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        val newStr = strength * (1.0 - (victim.getAttribute(Attribute.KNOCKBACK_RESISTANCE)?.value ?: 0.0))
        if (newStr <= 0) return
        val direction = damager.velocity
            .setY(0.0)
            .normalize()
            .multiply(newStr)

        val oldVel = victim.velocity
        val newVel = Vector(
            oldVel.x / 2 + direction.x,
            (oldVel.y / 2 + newStr).coerceAtLeast(newStr),
            oldVel.z / 2 + direction.z
        )
        //println("Executing knockback")
        //println("before kb: ${victim.velocity}")
        victim.velocity = newVel
        //println("after kb: ${victim.velocity}")
        //Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {println("Final velocity: ${victim.velocity}")})
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "strength" to strength
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.PROJECTILE_KNOCKBACK
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return ProjectileKnockbackApply(
                map["strength"].asDouble()
            )
        }
    }
}